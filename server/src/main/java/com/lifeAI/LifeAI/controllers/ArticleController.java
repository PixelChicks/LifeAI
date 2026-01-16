package com.lifeAI.LifeAI.controllers;

import com.lifeAI.LifeAI.model.Article;
import com.lifeAI.LifeAI.services.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    // Get all articles: /api/articles?page=0&size=10
    @GetMapping
    public ResponseEntity<Page<Article>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(articleService.getAllArticles(page, size));
    }

    // Get by subcategory: /api/articles/subcategory/SomeCategory?page=0&size=10
    @GetMapping("/subcategory/{subCategory}")
    public ResponseEntity<Page<Article>> getBySubCategory(
            @PathVariable String subCategory,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(articleService.getArticlesBySubCategory(subCategory, page, size));
    }
}