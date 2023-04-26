package com.example.diplomproject.model;

import lombok.Data;
import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@Table
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    private Ads ads;
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

}
