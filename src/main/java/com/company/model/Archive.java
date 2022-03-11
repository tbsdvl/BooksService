package com.company.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "archive")
public class Archive {
//    Test

    @Id
    @Column(name = "archive_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int archiveId;
    @NotEmpty(message = "archive name must not be empty")
    private String archiveName;

    @OneToMany(cascade=ALL, mappedBy="archiveId")
    public Set<Article> articles;

    public Archive(String archiveName) {
        this.archiveName = archiveName;
    }

    public Archive() {}

    public int getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(int archiveId) {
        this.archiveId = archiveId;
    }

    public String getArchiveName() {
        return archiveName;
    }

    public void setArchiveName(String archiveName) {
        this.archiveName = archiveName;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Archive archive = (Archive) o;
        return archiveId == archive.archiveId && Objects.equals(archiveName, archive.archiveName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(archiveId, archiveName);
    }

    @Override
    public String toString() {
        return "Archive{" +
                "archiveId=" + archiveId +
                ", archiveName='" + archiveName + '\'' +
                ", articles=" + articles +
                '}';
    }
}
