package com.lifeAI.LifeAI.controllers;

import com.lifeAI.LifeAI.model.Article;
import com.lifeAI.LifeAI.model.dto.common.ArticleCardDTO;
import com.lifeAI.LifeAI.services.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<Page<ArticleCardDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(articleService.getAllArticles(page, size));
    }

    @GetMapping("/subcategory/{subCategory}")
    public ResponseEntity<Page<ArticleCardDTO>> getBySubCategory(
            @PathVariable String subCategory,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(articleService.getArticlesBySubCategory(subCategory, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getById(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.getArticleById(id));
    }

    @GetMapping("/random/no-subcategory")
    public ResponseEntity<List<ArticleCardDTO>> getRandomNoSubCategory() {
        List<ArticleCardDTO> randomArticles = articleService.getRandomArticlesWithNoSubCategory(3);
        return ResponseEntity.ok(randomArticles);
    }

}