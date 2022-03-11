package com.company.repository;

import com.company.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer>{

    List<Article> findAllArticlesByArchive(int archiveId);
}
