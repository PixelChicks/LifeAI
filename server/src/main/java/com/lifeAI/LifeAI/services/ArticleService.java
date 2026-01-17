package com.lifeAI.LifeAI.services;

import com.lifeAI.LifeAI.model.Article;
import com.lifeAI.LifeAI.model.dto.common.ArticleCardDTO;
import org.springframework.data.domain.Page;

public interface ArticleService {
    Page<ArticleCardDTO> getAllArticles(int page, int size);
    Page<ArticleCardDTO> getArticlesBySubCategory(String subCategory, int page, int size);
    Article getArticleById(Long id);
}