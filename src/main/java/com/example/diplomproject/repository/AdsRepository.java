package com.example.diplomproject.repository;

import com.example.diplomproject.model.Ads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdsRepository extends JpaRepository<Ads, Integer> {

    List<Ads> findAllByAuthor_Username(String username);
    List<Ads> findByTitleContainingIgnoreCase(String text);

    List<Ads> findAdsByAuthor_Id(Integer authorId);

}
