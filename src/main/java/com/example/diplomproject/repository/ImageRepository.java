package com.example.diplomproject.repository;

import com.example.diplomproject.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
//    Optional<Image> findById(Long id);
//
//    void deleteAllByBytes(byte[] image);
}
