package com.company.repository;

import com.company.model.Article;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ArticleRepositoryTests {
    @Autowired
    ArticleRepository articleRepository;

    private Article article;
    private Article article2;


    @Before
    public void setUp() throws Exception {
        articleRepository.deleteAll();

        article = new Article();

        article.setArticleId(1);
        article.setArchiveId(6);
        article.setTitle("Testing with Junit");
        article.setLink("www.test.com");
        article.setSnippet("test snippet");
        article.setSummary("Testing");

        article2 = new Article();

        article2.setArticleId(1);
        article2.setArchiveId(7);
        article2.setTitle("Mocking with Mockify");
        article2.setLink("www.mock.com");
        article2.setSnippet("mock snippet");
        article2.setSummary("Mocking");
    }

    @Test
    public void shouldGetArticleByArticleIdFromDatabase() {

        article = articleRepository.save(article);
        System.out.println(article.getTitle());
        Article fromRepo = articleRepository.findById(article.getArticleId()).get();

        assertEquals(article.getTitle(), fromRepo.getTitle());
    }

    @Test
    public void shouldGetAllArticlesFromDatabase() {

        article = articleRepository.save(article);
        article2 = articleRepository.save(article2);
        List<Article> articleList = articleRepository.findAll();

        assertEquals(articleList, articleRepository.findAll());
    }

    @Test
    public void shouldDeleteArticleFromDatabase() {

        article = articleRepository.save(article);
        article2 = articleRepository.save(article2);
        articleRepository.deleteById(article2.getArticleId());
        Optional<Article> fromRepo = articleRepository.findById(article2.getArticleId());

        assertFalse(fromRepo.isPresent());
    }
}
