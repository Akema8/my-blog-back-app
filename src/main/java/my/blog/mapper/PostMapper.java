package my.blog.mapper;

import my.blog.dto.CommentDto;
import my.blog.dto.PostDto;
import my.blog.dto.PostUpdateRequestDto;
import my.blog.model.Comment;
import my.blog.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {Converters.class})
public interface PostMapper {

    @Mapping(source = "tags", target = "tags", qualifiedByName = "stringToList")
    PostDto toPostDTO(Post post);

    @Mapping(source = "tags", target = "tags", qualifiedByName = "listToString")
    Post toPost(PostDto postDto);

    @Mapping(source = "tags", target = "tags", qualifiedByName = "listToString")
    Post toPost(PostUpdateRequestDto updatedPost);

    CommentDto toCommentDTO(Comment comment);

    Comment toComment(CommentDto comment);
}