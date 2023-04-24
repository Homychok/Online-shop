package com.example.diplomproject.dto;

import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class ResponseWrapperComment {
    @MyAnnotation(name = "общее количество комментариев")
    private Integer count;
    private List<CommentDTO> result;
    public static  ResponseWrapperComment fromCommentDTO(List<CommentDTO> results) {
        ResponseWrapperComment responseWrapperComment = new ResponseWrapperComment();
        if (results == null) {
            return responseWrapperComment;
        }
        responseWrapperComment.setResult(results);
        responseWrapperComment.setCount(results.size());
        return responseWrapperComment;
    }
}
