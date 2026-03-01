package my.blog.service;

import my.blog.dto.PostDto;
import my.blog.dto.PostRequestDto;
import my.blog.dto.PostUpdateRequestDto;
import my.blog.dto.PostsResponseDto;
import my.blog.exception.NotFoundException;
import my.blog.mapper.PostMapper;
import my.blog.model.Post;
import my.blog.repository.PostRepository;
import my.blog.utility.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    public PostService(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    public PostDto getPostById(Long id) {
        Validator.validatePostId(id);
        PostDto post = postRepository.getById(id)
                .map(postMapper::toPostDTO)
                .orElseThrow(() -> new NotFoundException(id));
        return post;
    }

    public PostsResponseDto getPosts(String search, int pageNumber, int pageSize) {
        if (pageNumber <= 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Page number and size must be greater than zero");
        }
        List<PostDto> posts = postRepository.findAll().stream()
                .map(postMapper::toPostDTO)
                .toList();

        if (search != null && !search.trim().isEmpty()) {
            String[] words = search.trim().split("\\s+");
            List<String> filteredWords = Arrays.stream(words)
                    .filter(w -> !w.isEmpty())
                    .toList();
            List<String> tags = filteredWords.stream()
                    .filter(w -> w.startsWith("#"))
                    .map(w -> w.substring(1)) // убираем #
                    .toList();

            if (!tags.isEmpty()) {
                posts = posts.stream()
                        .filter(post -> {
                            List<String> postTags = post.getTags();
                            return postTags.containsAll(tags);
                        })
                        .toList();
            }

            String titleSearch = filteredWords.stream()
                    .filter(w -> !w.startsWith("#"))
                    .collect(Collectors.joining(" "))
                    .toLowerCase();
            if (!titleSearch.isEmpty()) {
                posts = posts.stream()
                        .filter(post -> post.getTitle() != null && post.getTitle().toLowerCase().contains(titleSearch))
                        .toList();
            }
        }

        int totalPosts = posts.size();
        int fromIndex = Math.min((pageNumber-1) * pageSize, totalPosts);
        int toIndex = Math.min(fromIndex + pageSize, totalPosts);

        List<PostDto> pagePosts = posts.subList(fromIndex, toIndex);

        boolean hasNext = toIndex < totalPosts;
        boolean hasPrevious = fromIndex > 0;
        return new PostsResponseDto(pagePosts, hasPrevious, hasNext, totalPosts);

    }

    @Transactional
    public PostDto savePost(PostRequestDto newPost) {
        Validator.validatePostRequest(newPost);
        String tags = "";
        if (newPost.getTags() != null && !newPost.getTags().isEmpty()) {
            tags = String.join(",", newPost.getTags());
        }
        long postId = postRepository.save(newPost.getTitle(), newPost.getText(), tags);
        return getPostById(postId);
    }

    @Transactional
    public PostDto updatePost(PostUpdateRequestDto updatedPost){
        Validator.validatePostUpdate(updatedPost);
        postRepository.update(postMapper.toPost(updatedPost));
        return getPostById(updatedPost.getId());
    }

    public void deletePost(long id) {
        Validator.validatePostId(id);
        if (postRepository.getById(id) == null) {
            throw new IllegalArgumentException("Post with ID " + id + " not found");
        }
        postRepository.deleteById(id);
    }

    public int likePost(long id){
        Validator.validatePostId(id);
        if (postRepository.getById(id) == null) {
            throw new IllegalArgumentException("Post with ID " + id + " not found");
        }
        postRepository.addLike(id);
        PostDto post = getPostById(id);
        return post.getLikesCount();
    }
}
