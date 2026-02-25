package my.blog.unit.test.service.configuration;

import my.blog.mapper.PostMapper;
import my.blog.repository.PostRepository;
import my.blog.service.CommentService;
import my.blog.service.ImageService;
import my.blog.service.PostService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import static org.mockito.Mockito.mock;

@Configuration
@PropertySource("classpath:test-application.properties")
@ComponentScan("my.blog.unit.test")
public class TestConfig {

    @Bean
    @Primary
    public PostRepository mockPostRepository() {
        return mock(PostRepository.class);
    }

    @Bean
    @Primary
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
    public ImageService imageService(@Value("${upload.dir.test}") String uploadDirPath){
        return new ImageService(uploadDirPath);
    }
}