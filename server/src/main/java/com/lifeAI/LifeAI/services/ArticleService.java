package com.lifeAI.LifeAI.services;

import com.lifeAI.LifeAI.model.Article;
import org.springframework.data.domain.Page;

public interface ArticleService {
    Page<Article> getAllArticles(int page, int size);
    Page<Article> getArticlesBySubCategory(String subCategory, int page, int size);
}
