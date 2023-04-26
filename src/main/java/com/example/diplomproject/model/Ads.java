package com.example.diplomproject.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table
public class Ads {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private Integer price;
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;
    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] image;
//    @JsonIgnore
    @OneToMany(mappedBy = "ads", cascade = CascadeType.ALL)
    private List<Comment> comments;

}