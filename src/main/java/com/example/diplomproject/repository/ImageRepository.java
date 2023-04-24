package com.example.diplomproject.repository;

import com.example.diplomproject.model.Avatar;
import com.example.diplomproject.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
//    Optional<Avatar> findById(Long id);
//
//    void deleteById(Long id);
//
//    void deleteAllByData(byte[] image);
}
