package com.lifeAI.LifeAI.services.impl;

import com.lifeAI.LifeAI.model.Article;
import com.lifeAI.LifeAI.respository.ArticleRepository;
import com.lifeAI.LifeAI.services.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Override
    public Page<Article> getAllArticles(int page, int size) {
        return articleRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Page<Article> getArticlesBySubCategory(String subCategory, int page, int size) {
        return articleRepository.findBySubCategory(subCategory, PageRequest.of(page, size));
    }
}