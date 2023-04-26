package com.example.diplomproject.repository;

import com.example.diplomproject.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Optional<Comment> findByIdAndAdsId(Integer id, Integer adsId);

    List<Comment> findAllByAdsId (Integer adsId);

    void deleteByIdAndAdsId(Integer id, Integer adsId);
}