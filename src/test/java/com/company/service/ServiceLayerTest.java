package com.company.service;

import com.company.model.Archive;
import com.company.model.Article;
import com.company.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceLayerTest {

    //    Instantiate ServiceLayer when needed
    @Autowired
    ServiceLayer service;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ServiceLayer serviceLayer;

    private ObjectMapper mapper = new ObjectMapper();

    //    Declare mock repositories for Archive & Article
    @MockBean
    ArticleRepository articleRepository;

    @MockBean
    ArchiveRepository archiveRepository;


    @MockBean

    Article inputArticle;
    Article outputArticle;
    List<Article> articles;

    Archive inputArchive;
    Archive outputArchive;
    List<Archive> archives;

    @Before
    public void setUp() throws Exception {
        //  Initialize input objects & output objects

        //   Article
        inputArticle = new Article(
                1,
                "Population biology of plants.",
                "https://www.cabdirect.org/cabdirect/abstract/19782321379",
                "The first chapter is concerned with experiments, analogies and models.",
                "JL Harper - Population biology of plants., 1977 - cabdirect.org"
        );
        outputArticle = new Article();
        outputArticle.setArticleId(1);
        articles = new ArrayList<>();
        articles.add(outputArticle);

        // Article set
        Set<Article> archiveArticles = new HashSet<>();
        archiveArticles.add(inputArticle);

        //   Archive
        inputArchive = new Archive("Biology");
        inputArchive.setArticles(archiveArticles);

        outputArchive = new Archive();
        outputArchive.setArchiveId(1);
        outputArchive.setArchiveName("Biology");
        outputArchive.setArticles(archiveArticles);

        archives = new ArrayList<>();
        archives.add(outputArchive);
    }

    @Test
    public void shouldCreateANewArchiveToDatabase() {

        when(archiveRepository.save(inputArchive)).thenReturn(outputArchive);

        Archive fromServiceArchive = service.saveArchive(inputArchive);
        assertEquals(fromServiceArchive.getArchiveId(), outputArticle.getArticleId());
    }

    @Test
    public void shouldFindAnArchiveByArchiveIdInTheDatabase() {

        when(archiveRepository.findById(inputArchive.getArchiveId())).thenReturn(Optional.ofNullable(outputArchive));

        Archive fromServiceArchive = service.findArchive(inputArchive.getArchiveId()).get();
        assertEquals(fromServiceArchive.getArchiveId(), outputArchive.getArchiveId());
    }

    @Test
    public void shouldReturnAllArchivesInTheDatabase() {

        when(archiveRepository.findAll()).thenReturn(archives);

        List<Archive> fromServiceArchives = archiveRepository.findAll();
        assertEquals(fromServiceArchives.size(), archives.size());
    }

    @Test
    public void shouldUpdateAnArchiveNameInDatabase() {

        Archive updatedArchive = new Archive();
        updatedArchive.setArchiveId(outputArchive.getArchiveId());
        updatedArchive.setArchiveName("Psychology");
        updatedArchive.setArticles(inputArchive.getArticles());

        when(archiveRepository.findById(outputArchive.getArchiveId())).thenReturn(Optional.ofNullable(outputArchive));
        when(archiveRepository.save(updatedArchive)).thenReturn(updatedArchive);

        Archive foundArchive = archiveRepository.findById(outputArchive.getArchiveId()).get();

        Archive fromServiceUpdatedArchive = service.updateArchive(updatedArchive);

        assertEquals(foundArchive.getArchiveId(), fromServiceUpdatedArchive.getArchiveId());
        assertEquals(foundArchive.getArticles().size(), fromServiceUpdatedArchive.getArticles().size());
    }

    @Test
    public void shouldDeleteAnArchiveInTheDatabase() {

        when(archiveRepository.findById(outputArchive.getArchiveId())).thenReturn(Optional.ofNullable(outputArchive));
        doNothing().when(archiveRepository).delete(outputArchive);
        service.deleteArchive(outputArchive.getArchiveId());
    }

    @Test
    public void shouldSaveAnArticleToAnArchiveInDatabase() {

        when(articleRepository.findById(inputArticle.getArticleId())).thenReturn(Optional.ofNullable(outputArticle));
        when(articleRepository.save(inputArticle)).thenReturn(outputArticle);

        Article saveArticle = service.saveArticle(inputArticle);
        Article fromServiceArticle = service.findArticle(inputArticle.getArticleId());
        assertEquals(fromServiceArticle.getArticleId(), saveArticle.getArticleId());
    }

    @Test
    public void shouldReturnAnArticleFromTheDatabase() {

        when(articleRepository.findById(inputArticle.getArticleId())).thenReturn(Optional.ofNullable(outputArticle));

        Article fromServiceArticle = service.findArticle(inputArticle.getArticleId());
        assertEquals(fromServiceArticle.getArticleId(), outputArticle.getArticleId());
    }

    @Test
    public void shouldReturnAllArticlesFromTheDatabase() {

        when(articleRepository.findAll()).thenReturn(articles);

        List<Article> fromServiceArticles = service.findAllArticles();
        assertEquals(fromServiceArticles.get(0).getArticleId(), outputArticle.getArticleId());
    }

    @Test
    public void shouldReturnAllArticlesBelongingToAnArchive() {

        // Instantiate the found article from inputArchive
        Article foundArticle = new Article(
                7,
                "Population biology of plants.",
                "https://www.cabdirect.org/cabdirect/abstract/19782321379",
                "The first chapter is concerned with experiments, analogies and models.",
                "JL Harper - Population biology of plants., 1977 - cabdirect.org"
        );

        //  Article's archive
        Archive newArchive = new Archive("Biology");
        newArchive.setArchiveId(7);

        // Archive's article set
        Set<Article> archiveArticles = new HashSet<>();
        archiveArticles.add(foundArticle);
        newArchive.setArticles(archiveArticles);

        List<Article> foundArticles = new ArrayList<>();
        foundArticles.add(foundArticle);

        when(archiveRepository.findById(newArchive.getArchiveId())).thenReturn(Optional.ofNullable(newArchive));
        when(articleRepository.findAllArticlesByArchive(newArchive.getArchiveId())).thenReturn(foundArticles);

        List<Article> fromServiceUserArticles = service.findArchiveArticles(newArchive.getArchiveId());
        assertEquals(fromServiceUserArticles.size(), archiveArticles.size());
    }

    @Test
    public void shouldUpdateArticleTest(){

        Article originalArticle = new Article();
        originalArticle.setTitle("title");
        originalArticle.setArticleId(1);
        originalArticle.setSummary("summary");
        originalArticle.setSnippet("snippet");
        originalArticle.setLink("link");
        originalArticle.setArchiveId(1);

        //create an example of what would be the updated object (this is a fake version that will be used as a reference for the test)
        Article updatedArticle = new Article();
        updatedArticle.setTitle("title");
        updatedArticle.setArticleId(1);
        updatedArticle.setSummary("updated summary");
        updatedArticle.setSnippet("snippet");
        updatedArticle.setLink("link");
        updatedArticle.setArchiveId(1);

        //then we will mock the function of looking in the database for the "original/non-updated" object
        when(articleRepository.findById(originalArticle.getArticleId())).thenReturn(Optional.ofNullable(originalArticle));

        //then mock the save/update function using the object we instantiated in step 1 (updated article)
        when(articleRepository.save(updatedArticle)).thenReturn(updatedArticle);

        //then find that new/updated article in the database
        Article fromServiceUpdatedArticle = service.updateArticle(updatedArticle);

        assertEquals(fromServiceUpdatedArticle.getSummary(), updatedArticle.getSummary());
    }

    @Test
    public void shouldDeleteAnArticleFromTheDatabase() {

        Article articleToDelete = new Article(
                40,
                "Population biology of plants.",
                "https://www.cabdirect.org/cabdirect/abstract/19782321379",
                "The first chapter is concerned with experiments, analogies and models.",
                "JL Harper - Population biology of plants., 1977 - cabdirect.org"
        );

        when(articleRepository.findById(inputArticle.getArticleId())).thenReturn(Optional.ofNullable(articleToDelete));
        doNothing().when(articleRepository).delete(articleToDelete);
        service.deleteArticle(articleToDelete.getArticleId());
    }


}