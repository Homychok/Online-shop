package com.example.diplomproject.dto;

import com.example.diplomproject.annotations.MyAnnotation;
import lombok.Data;
import java.time.Instant;

@Data
public class CommentDTO {
    @MyAnnotation(name = "id автора комментария")
    private Integer author;
@MyAnnotation(name = "ссылка на аватар автора комментария")
private String authorImage;
@MyAnnotation(name = "имя создателя комментария")
private String authorFirstName;
@MyAnnotation(name = "дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970")
private Instant createdAt;
@MyAnnotation(name = "id комментария")
private Integer pk;
    @MyAnnotation(name = "текст комментария")
    private String text;
}
