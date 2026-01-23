package com.lifeAI.LifeAI.respository;

import com.lifeAI.LifeAI.model.Article;
import com.lifeAI.LifeAI.model.dto.common.ArticleCardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("SELECT new com.lifeAI.LifeAI.model.dto.common.ArticleCardDTO(" +
            "a.id, a.title, " +
            "CASE WHEN LENGTH(a.description) > 150 THEN CONCAT(SUBSTRING(a.description, 1, 150), '...') " +
            "ELSE a.description END, " +
            "a.thumbnailPicture, a.subCategory, a.calories, a.hours, a.minutes) " +
            "FROM Article a " +
            "WHERE a.deletedAt IS NULL AND a.subCategory IS NOT NULL " +
            "ORDER BY a.createdAt DESC")
    Page<ArticleCardDTO> findAllCards(Pageable pageable);

    @Query("SELECT new com.lifeAI.LifeAI.model.dto.common.ArticleCardDTO(" +
            "a.id, a.title, " +
            "CASE WHEN LENGTH(a.description) > 150 THEN CONCAT(SUBSTRING(a.description, 1, 150), '...') " +
            "ELSE a.description END, " +
            "a.thumbnailPicture, a.subCategory, a.calories, a.hours, a.minutes) " +
            "FROM Article a " +
            "WHERE a.subCategory = :subCategory AND a.deletedAt IS NULL " +
            "ORDER BY a.createdAt DESC")
    Page<ArticleCardDTO> findCardsBySubCategory(@Param("subCategory") String subCategory, Pageable pageable);

    @Query("SELECT new com.lifeAI.LifeAI.model.dto.common.ArticleCardDTO(" +
            "a.id, a.title, " +
            "CASE WHEN LENGTH(a.description) > 150 THEN CONCAT(SUBSTRING(a.description, 1, 150), '...') " +
            "ELSE a.description END, " +
            "a.thumbnailPicture, a.subCategory, a.calories, a.hours, a.minutes) " +
            "FROM Article a " +
            "WHERE a.subCategory IS NULL AND a.deletedAt IS NULL " +
            "ORDER BY function('RAND')")
    List<ArticleCardDTO> findRandomArticlesWithNoSubCategory(Pageable pageable);
}