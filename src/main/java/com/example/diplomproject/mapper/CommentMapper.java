package com.example.diplomproject.mapper;

import com.example.diplomproject.dto.CommentDTO;
import com.example.diplomproject.model.Comment;

public class CommentMapper {
    public static Comment fromDTO(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setPk(commentDTO.getPk());
        comment.setText(commentDTO.getText());
        return comment;
    }

    public static CommentDTO toDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setPk(comment.getPk());
        commentDTO.setText(comment.getText());
        commentDTO.setAuthorImage("/users/me/image/" + comment.getAuthor().getId());
        commentDTO.setAuthor(comment.getAuthor().getId());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setAuthorFirstName(comment.getAuthor().getFirstName());
        return commentDTO;
    }
}
