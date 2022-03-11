package com.company.service;

import com.company.exceptions.NotFoundException;
import com.company.model.Archive;
import com.company.model.Article;
import com.company.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ServiceLayer {
    private ArchiveRepository archiveRepository;
    private ArticleRepository articleRepository;

    @Autowired
    public ServiceLayer( ArchiveRepository archiveRepository, ArticleRepository articleRepository) {
        this.archiveRepository = archiveRepository;
        this.articleRepository = articleRepository;
    }

    //    Archive ServiceLayer Tests
    public Archive saveArchive(Archive archive) {
        return archiveRepository.save(archive);
    }

    public Optional<Archive> findArchive(int id) {

        Optional<Archive> foundArchive = archiveRepository.findById(id);

        if(foundArchive == null) {
            throw new IllegalArgumentException("Archive does not exist in the database.");
        }

        return foundArchive;
    }

    public List<Archive> findAllArchives() {
        return archiveRepository.findAll();
    }

    public Archive updateArchive(Archive archive) {
        // look for archive in db
        Archive foundArchive = archiveRepository.findById(archive.getArchiveId()).get();

        if(foundArchive.getArchiveName() == null) {
            throw new IllegalArgumentException("The archive does not exist in the database.");
        }

        foundArchive.setArchiveId(archive.getArchiveId());
        foundArchive.setArchiveName(archive.getArchiveName());
        foundArchive.setArticles(archive.getArticles());
        return archiveRepository.save(foundArchive);
    }

    public void deleteArchive(int id) {

        Archive foundArchive = archiveRepository.findById(id).get();

        if(foundArchive.getArchiveName() == null) {
            throw new IllegalArgumentException("Archive does not exist in the database.");
        }

        archiveRepository.delete(foundArchive);
        System.out.println("Deleted the archive " + foundArchive.getArchiveName());
    }

    public Article saveArticle(Article article) {

        return articleRepository.save(article);
    }

    public Article findArticle(int id) {

        Article foundArticle = articleRepository.findById(id).get();
        return foundArticle;
    }

    public List<Article> findAllArticles() {
        return articleRepository.findAll();
    }

    public List<Article> findArchiveArticles(int archiveId) {

        Archive foundArchive = archiveRepository.findById(archiveId).get();

        if (foundArchive.getArchiveName() == null) {
            throw new IllegalArgumentException("Archive does not exist in the database");
        }

        List<Article> articleList = articleRepository.findAllArticlesByArchive(archiveId);

        return articleList;
    }

    public void deleteArticle(int id) {

        Article foundArticle = articleRepository.findById(id).get();

        if (foundArticle.getTitle() == null) {
            throw new IllegalArgumentException("Cannot delete, article does not exist in database.");
        }

        articleRepository.delete(foundArticle);
    }

    public Article updateArticle(Article article){

        Article foundArticle = articleRepository.findById(article.getArticleId()).get();

        if(foundArticle == null){
            throw new IllegalArgumentException("article does not exist in the archive.");
        }

        //  Update the found article
        foundArticle.setArticleId(article.getArticleId());
        foundArticle.setSummary(article.getSummary());
        foundArticle.setSnippet(article.getSnippet());
        foundArticle.setLink(article.getLink());
        foundArticle.setTitle(article.getTitle());
        return articleRepository.save(foundArticle);
    }
}