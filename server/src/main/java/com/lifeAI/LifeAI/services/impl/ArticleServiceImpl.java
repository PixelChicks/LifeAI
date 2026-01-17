package com.lifeAI.LifeAI.services.impl;

import com.lifeAI.LifeAI.model.Article;
import com.lifeAI.LifeAI.model.dto.common.ArticleCardDTO;
import com.lifeAI.LifeAI.respository.ArticleRepository;
import com.lifeAI.LifeAI.services.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;

    @Override
    public Page<ArticleCardDTO> getAllArticles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return articleRepository.findAllCards(pageable);
    }

    @Override
    public Page<ArticleCardDTO> getArticlesBySubCategory(String subCategory, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return articleRepository.findCardsBySubCategory(subCategory, pageable);
    }

    @Override
    public Article getArticleById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Article with id " + id + " not found"));
    }
}