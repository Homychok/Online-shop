package com.example.diplomproject.dto;

import com.example.diplomproject.exception.ImageNotFoundException;
import com.example.diplomproject.model.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

@Data
public class CommentDTO {
    @MyAnnotation(name = "id автора комментария")
    private Integer author;
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
@MyAnnotation(name = "ссылка на аватар автора комментария")
private String authorImage;
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
@MyAnnotation(name = "имя создателя комментария")
private String authorFirstName;
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
@MyAnnotation(name = "дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970")
private Instant createdAt;
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
@MyAnnotation(name = "id комментария")
private Integer pk;
    @MyAnnotation(name = "текст комментария")
    private String text;
    public static CommentDTO fromCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setAuthor(comment.getAuthor().getId());
        if (comment.getAuthor().getAvatar() == null){
            commentDTO.setAuthorImage("No image");
            throw new ImageNotFoundException();
        } else {
            commentDTO.setAuthorImage("/ads/me/image/"
                    + comment.getAuthor().getAvatar().getId());
        }
        commentDTO.setAuthorFirstName(comment.getAuthor().getFirstName());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setPk(comment.getId());
        commentDTO.setText(comment.getText());
        return commentDTO;
    }

    public Comment toComment() {
        Comment comment = new Comment();
        comment.setId(this.getPk());
        comment.setCreatedAt(this.getCreatedAt());
        comment.setText(this.getText());
        return comment;
    }
}
