package my.blog.service;

import my.blog.dto.*;
import my.blog.mapper.PostMapper;
import my.blog.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

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

    @Transactional
    public PostDto updatePost(PostUpdateRequestDto updatedPost){
        postRepository.update(postMapper.toPost(updatedPost));
        return getPostById(updatedPost.getId());
    }

    public void deletePost(long id) {
        postRepository.deleteById(id);
    }

    @Transactional
    public int likePost(long id){
        postRepository.addLike(id);
        PostDto post = getPostById(id);
        return post.getLikesCount();
    }


}
