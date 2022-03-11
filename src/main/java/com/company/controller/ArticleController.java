
package com.company.controller;

import com.company.model.Article;
import com.company.repository.ArticleRepository;
import com.company.service.ServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ArticleController {

    @Autowired
    private ServiceLayer serviceLayer;
//        private ArticleRepository articleRepository;

    @GetMapping("/article") // find all articles
    public List<Article> getAllArticles() {
        return serviceLayer.findAllArticles();
    }

    @GetMapping("/article/{articleId}") // find article by String articleId
    public Article getArticleById(@PathVariable int articleId) {
        return serviceLayer.findArticle(articleId);
    }

    @DeleteMapping("article/{articleId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteArticleById(@PathVariable int articleId) {
        serviceLayer.deleteArticle(articleId);
    }
}