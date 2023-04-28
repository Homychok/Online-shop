package com.example.diplomproject.dto;

import com.example.diplomproject.annotations.MyAnnotation;
import lombok.Data;
import java.util.List;

@Data
public class ResponseWrapperComment {
    @MyAnnotation(name = "общее количество комментариев")
    private Integer count;
    private List<CommentDTO> result;
}
