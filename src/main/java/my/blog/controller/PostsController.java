package my.blog.controller;

import my.blog.dto.PostDto;
import my.blog.dto.PostRequestDto;
import my.blog.dto.PostUpdateRequestDto;
import my.blog.dto.PostsResponseDto;
import my.blog.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostsController {

    private final PostService postService;

    public PostsController(PostService _postService) { this.postService = _postService; }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    ///api/posts?search=Lalala&pageNumber=1&pageSize=5
    @GetMapping
    public ResponseEntity<PostsResponseDto> getPosts(
            @RequestParam(name = "search") String search,
            @RequestParam(name = "pageNumber") int pageNumber,
            @RequestParam(name = "pageSize") int pageSize) {
        return ResponseEntity.ok(postService.getPosts(search, pageNumber, pageSize));
    }

    @PostMapping
    public ResponseEntity<PostDto> savePost(@RequestBody PostRequestDto newPost){
        return ResponseEntity.ok(postService.savePost(newPost));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostUpdateRequestDto updatedPost) {
        return ResponseEntity.ok(postService.updatePost(updatedPost));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable(name = "id") Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{id}/likes")
    public ResponseEntity<Integer> likePost(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(postService.likePost(id));
    }

}
