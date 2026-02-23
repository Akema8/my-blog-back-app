package my.blog.unit.test.service.configuration;

import my.blog.mapper.PostMapper;
import my.blog.repository.PostRepository;
import my.blog.service.CommentService;
import my.blog.service.ImageService;
import my.blog.service.PostService;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan("my.blog.unit.test")
public class TestConfig {

    @Bean
    @Primary
    public PostRepository mockPostRepository() {
        return mock(PostRepository.class);
    }

    @Bean
    public PostMapper postMapper() {
        return Mappers.getMapper(PostMapper.class);
    }

    @Bean
    public PostService postService(PostRepository postRepository, PostMapper postMapper) {
        return new PostService(postRepository, postMapper);
    }

    @Bean
    public CommentService commentService(PostRepository postRepository, PostMapper postMapper){
        return new CommentService(postRepository, postMapper);
    }

    @Bean
    public ImageService imageService(){
        return new ImageService("test_uploads/");
    }
}