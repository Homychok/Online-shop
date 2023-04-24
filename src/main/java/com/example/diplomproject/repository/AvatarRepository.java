package com.example.diplomproject.repository;

import com.example.diplomproject.model.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Integer> {
//    Optional<Avatar> findById(Long id);
//
//    void deleteById(Long id);
//
//    void deleteAllByData(byte[] image);
}