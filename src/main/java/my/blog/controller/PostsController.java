package my.blog.controller;

import my.blog.dto.*;
import my.blog.service.ImageService;
import my.blog.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/posts")
public class PostsController {

    private final PostService postService;
    private final ImageService imageService;

    public PostsController(PostService postService, ImageService imageService) {
        this.postService = postService;
        this.imageService = imageService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping
    public ResponseEntity<PostsResponseDto> getPosts(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
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

    @PostMapping(value = "/{id}/image")
    public ResponseEntity<Void> addImage(@PathVariable(name = "id") Long id, @RequestParam("image") MultipartFile image){
        imageService.uploadImage(id, image);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> downloadImage(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(imageService.downloadImage(id));
    }

    @PutMapping(value = "/{id}/image")
    public ResponseEntity<Void> updateImage(@PathVariable(name = "id") Long id, @RequestParam("image") MultipartFile image){
        imageService.updateImage(id, image);
        return ResponseEntity.ok().build();
    }


}
