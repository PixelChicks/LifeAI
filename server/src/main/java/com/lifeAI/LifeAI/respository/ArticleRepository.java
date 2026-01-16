package com.lifeAI.LifeAI.respository;

import com.lifeAI.LifeAI.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findBySubCategory(String subCategory, Pageable pageable);
}