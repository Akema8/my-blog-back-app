package my.blog.unit.test.service;

import my.blog.dto.PostDto;
import my.blog.dto.PostRequestDto;
import my.blog.dto.PostUpdateRequestDto;
import my.blog.dto.PostsResponseDto;
import my.blog.model.Post;
import my.blog.repository.PostRepository;
import my.blog.service.PostService;
import my.blog.unit.test.service.configuration.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class PostServiceTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostService postService;

    @BeforeEach
    void resetMocks() {
        reset(postRepository);
    }

    @Test
    void testGetPostById() {
        long postId = 1L;
        Post post = new Post(postId, "Title1", "Text1", "tag1, tag2", 0, 0);
        PostDto postDto = new PostDto(postId, "Title1", "Text1", Arrays.asList("tag1", "tag2"), 0, 0);
        when(postRepository.getById(postId)).thenReturn(post);

        PostDto result = postService.getPostById(postId);
        verify(postRepository).getById(postId);
        assertNotNull(result, "PostService должен возвращать пост");
        assertEquals(postDto, result);
    }

    @Test
    void testGetPosts_noSearchParam_success() {
        List<Post> posts = List.of(
                new Post(1L, "Title1", "Text1", "tag1,tag2", 0, 0),
                new Post(2L, "Title2", "Text2", "tag2,tag3", 0, 0)
        );

        when(postRepository.findAll()).thenReturn(posts);

        PostsResponseDto response = postService.getPosts(null, 1, 10);
        assertFalse(response.isHasPrev());
        assertFalse(response.isHasNext());
        assertEquals(2, response.getPosts().size());

        for (PostDto dto : response.getPosts()) {
            assertNotNull(dto);
            assertTrue(dto.getTitle().startsWith("Title"));
        }
    }

    @Test
    void testGetPosts_withSearchTags_success() {
        List<Post> posts = List.of(
                new Post(1L, "Title1", "Text1", "tag1,tag2", 0, 0),
                new Post(2L, "Title2", "Text2", "tag2,tag3", 0, 0)
        );

        when(postRepository.findAll()).thenReturn(posts);

        PostsResponseDto response = postService.getPosts("#tag2", 1, 10);

        assertEquals(2, response.getPosts().size());
        assertTrue(response.getPosts().stream().allMatch(p -> p.getTags().contains("tag2")));
    }

    @Test
    void testGetPosts_withTSearchParam_success() {
        List<Post> posts = List.of(
                new Post(1L, "First Post", "Text1", "tag1", 0, 0),
                new Post(2L, "Second Post", "Text2", "tag2", 0, 0)
        );

        when(postRepository.findAll()).thenReturn(posts);

        // Поиск по части заголовка "First"
        PostsResponseDto response = postService.getPosts("First", 1, 10);

        assertEquals(1, response.getPosts().size());
        assertTrue(response.getPosts().stream().allMatch(p -> p.getTitle().toLowerCase().contains("first")));
    }

    @Test
    void testSavePost_success() {
        PostRequestDto requestDto = new PostRequestDto("New Title", "New Text", List.of("tag1", "tag2"));
        Long newId = 123L;
        Post newPost = new Post(newId, "New Title", "New Text", "tag1,tag2", 0, 0);
        PostDto newPostDto = new PostDto(newId, "New Title", "New Text", List.of("tag1", "tag2"), 0, 0);

        when(postRepository.save(anyString(), anyString(), anyString())).thenReturn(newId);
        when(postRepository.getById(newId)).thenReturn(newPost);
        PostDto result = postService.savePost(requestDto);

        verify(postRepository).save("New Title", "New Text", "tag1,tag2");
        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
    }

    @Test
    void testUpdatePost_success() {
        PostUpdateRequestDto updateDto = new PostUpdateRequestDto(1L, "Updated Title", "Updated Text", List.of("tag1"));

        Post postToUpdate = new Post(1L, "Old Title", "Old Text", "tag1", 0, 0);
        Post updatedPost = new Post(1L, "Updated Title", "Updated Text", "tag1", 0, 0);
        PostDto updatedDto = new PostDto(1L, "Updated Title", "Updated Text", List.of("tag1"), 0, 0);

        doNothing().when(postRepository).update(updatedPost);
        when(postRepository.getById(1L)).thenReturn(updatedPost);
        PostDto result = postService.updatePost(updateDto);

        verify(postRepository).update(updatedPost);
        assertEquals("Updated Title", result.getTitle());
    }

    @Test
    void testDeletePost_success() {
        long id = 123L;
        Post post = new Post(id, "New Title", "New Text", "tag1,tag2", 0, 0);
        when(postRepository.getById(id)).thenReturn(post);
        doNothing().when(postRepository).deleteById(id);
        postService.deletePost(id);
        verify(postRepository).deleteById(id);
    }

    @Test
    void testLikePost_success() {
        long id = 1L;
        Post post = new Post(id, "Title", "Text", "tag", 5, 0);
        PostDto postDto = new PostDto(id, "Title", "Text", List.of("tag"), 5, 0);

        when(postRepository.getById(id)).thenReturn(post);
        doNothing().when(postRepository).addLike(id);
        int likesCount = postService.likePost(id);
        verify(postRepository).addLike(id);
        assertEquals(5, likesCount);
    }

    @Test
    void testGetPostById_notFound_exception() {
        long postId = 999L;
        when(postRepository.getById(postId)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.getPostById(postId);
        });
        assertEquals("Post with ID " + postId + " not found", exception.getMessage());
    }

    @Test
    void testGetPostById_invalidId_exception() {
        long invalidId = -1L;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.getPostById(invalidId);
        });
        assertEquals("postId must be positive", exception.getMessage());
    }

    @Test
    void testDeletePost_invalidId_exception() {
        long invalidId = 0;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.deletePost(invalidId);
        });
        assertEquals("postId must be positive", exception.getMessage());
    }

    @Test
    void testDeletePost_notFound_exception() {
        long id = 999L;
        when(postRepository.getById(id)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.deletePost(id);
        });
        assertEquals("Post with ID " + id + " not found", exception.getMessage());
    }

    @Test
    void testUpdatePost_notFound_exception() {
        PostUpdateRequestDto updateDto = new PostUpdateRequestDto(999L, "Title", "Text", List.of("tag"));

        when(postRepository.getById(999L)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.updatePost(updateDto);
        });
        assertEquals("Post with ID 999 not found", exception.getMessage());
    }

    @Test
    void testUpdatePost_invalidId_exception() {
        PostUpdateRequestDto updateDto = new PostUpdateRequestDto(0, "Title", "Text", List.of("tag"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.updatePost(updateDto);
        });
        assertEquals("postId must be positive", exception.getMessage());
    }

    @Test
    void testLikePost_invalidId_exception() {
        long invalidId = -5;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.likePost(invalidId);
        });
        assertEquals("postId must be positive", exception.getMessage());
    }

    @Test
    void testLikePost_notFound_exception() {
        long id = 999L;
        when(postRepository.getById(id)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.likePost(id);
        });
        assertEquals("Post with ID " + id + " not found", exception.getMessage());
    }
}
