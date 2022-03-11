package com.company.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "article")
public class Article {

    @Id
    @Column(name = "article_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int articleId;
    private int archiveId;
    private String title;
    private String link;
    private String snippet;
    private String summary;


    @ManyToOne
    @JoinColumn(name="archiveId", nullable=false, insertable = false, updatable = false)
    private Archive archive;

    public Article(int archiveId, String title, String link, String snippet, String summary) {
        this.archiveId = archiveId;
        this.title = title;
        this.link = link;
        this.snippet = snippet;
        this.summary = summary;
    }
    public Article(){};

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(int archiveId) {
        this.archiveId = archiveId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Archive getArchive() {
        return archive;
    }

    public void setArchive(Archive archive) {
        this.archive = archive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return articleId == article.articleId && Objects.equals(archiveId, article.archiveId) && Objects.equals(title, article.title) && Objects.equals(link, article.link) && Objects.equals(snippet, article.snippet) && Objects.equals(summary, article.summary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleId, archiveId, title, link, snippet, summary);
    }

    @Override
    public String toString() {
        return "Article{" +
                "articleId=" + articleId +
                ", archiveId='" + archiveId + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", snippet='" + snippet + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }
}
