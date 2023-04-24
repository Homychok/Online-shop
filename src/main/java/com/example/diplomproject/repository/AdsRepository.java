package com.example.diplomproject.repository;

import com.example.diplomproject.model.Ads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdsRepository extends JpaRepository<Ads, Integer> {

//    List<Ads> findAllByAuthorId(Integer id);
    List<Ads> findAllByAuthor_Username(String username);
    List<Ads> findByTitleContainingIgnoreCase(String text);

    @Modifying
    @Query(value = "UPDATE ads a SET image_id = ?2 WHERE a.id = ?1", nativeQuery = true)
    void updateAdsImage(Integer id, Integer imageId);

}
