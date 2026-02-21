package my.blog.service;

import my.blog.dto.PostDto;
import my.blog.dto.PostRequestDto;
import my.blog.dto.PostUpdateRequestDto;
import my.blog.dto.PostsResponseDto;
import my.blog.mapper.PostMapper;
import my.blog.model.Post;
import my.blog.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public PostService(PostRepository _postRepository, PostMapper _postMapper) {
        this.postRepository = _postRepository;
        this.postMapper = _postMapper;
    }

    public PostDto getPostById(Long id) {
        return postMapper.toPostDTO(postRepository.getById(id));
    }

    public PostsResponseDto getPosts(String search, int pageNumber, int pageSize) {
        List<PostDto> posts = postRepository.findAll().stream()
                .map(postMapper::toPostDTO)
                .toList();

        PostsResponseDto postsResponse = new PostsResponseDto(posts, true, true, 0);
        return postsResponse;
    }


    public PostDto savePost(PostRequestDto newPost) {
        String tags = "";
        if (newPost.getTags() != null && !newPost.getTags().isEmpty()) {
            tags = String.join(",", newPost.getTags());
        }
        long postId = postRepository.save(newPost.getTitle(), newPost.getText(), tags);
        return getPostById(postId);
    }

    public PostDto updatePost(PostUpdateRequestDto updatedPost){
        postRepository.update(postMapper.toPost(updatedPost));
        return getPostById(updatedPost.getId());
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
