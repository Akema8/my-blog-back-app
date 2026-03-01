package my.blog.controller;

import my.blog.dto.PostDto;
import my.blog.dto.PostRequestDto;
import my.blog.dto.PostUpdateRequestDto;
import my.blog.dto.PostsResponseDto;
import my.blog.service.ImageService;
import my.blog.service.PostService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
        PostDto ee = postService.getPostById(id);
        return ResponseEntity.ok(ee);
    }

    @GetMapping
    public ResponseEntity<PostsResponseDto> getPosts(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "pageNumber") int pageNumber,
            @RequestParam(name = "pageSize") int pageSize) {
        return ResponseEntity.ok(postService.getPosts(search, pageNumber, pageSize));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PostDto> savePost(@RequestBody PostRequestDto newPost){
        return ResponseEntity.ok(postService.savePost(newPost));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable(name = "id") Long id, @RequestBody PostUpdateRequestDto updatedPost) {
        updatedPost.setId(id);
        return ResponseEntity.ok(postService.updatePost(updatedPost));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletePost(@PathVariable(name = "id") Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/likes")
    public ResponseEntity<Integer> likePost(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(postService.likePost(id));
    }

    @PostMapping(value = "/{id}/image")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> addImage(@PathVariable(name = "id") Long id, @RequestParam("image") MultipartFile image){
        imageService.uploadImage(id, image);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> downloadImage(@PathVariable(name = "id") Long id) {
        MediaType mediaType = MediaType.IMAGE_JPEG;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return new ResponseEntity<>(imageService.downloadImage(id), headers, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/image")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateImage(@PathVariable(name = "id") Long id, @RequestParam("image") MultipartFile image){
        imageService.updateImage(id, image);
        return ResponseEntity.noContent().build();
    }
}