package com.company.controller;

import com.company.model.Archive;
import com.company.model.Article;
import com.company.service.ServiceLayer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ArticleController.class)
public class ArticleControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    ServiceLayer serviceLayer;

    private ObjectMapper mapper = new ObjectMapper();
    private Article inputArticle;
    private Article outputArticle;
    private List<Article> articleList;

    @Before
    public void setUp() throws Exception {
        inputArticle = new Article();
        inputArticle.setTitle("article title");
        inputArticle.setArchiveId(4);
        inputArticle.setArticleId(1);
        inputArticle.setLink("link to article");
        inputArticle.setSnippet("snippet of article");
        inputArticle.setSummary("summary of article");

        outputArticle = new Article();
        outputArticle.setTitle("article title");
        outputArticle.setArchiveId(4);
        outputArticle.setLink("link to article");

        articleList = new ArrayList<>();
        articleList.add(inputArticle);


    }

    @Test
    public void findAllArticlesTest() throws Exception{
        String outputString = mapper.writeValueAsString(articleList);

        when(serviceLayer.findAllArticles()).thenReturn(articleList);

        mockMvc.perform(get("/article")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputString));
    }

    @Test
    public void findArticleByIdTest() throws Exception{
        String inputString = mapper.writeValueAsString(inputArticle);
        String outputString = mapper.writeValueAsString(outputArticle);

        when(serviceLayer.findArticle(inputArticle.getArticleId())).thenReturn(outputArticle);

        mockMvc.perform(get("/article/1")
                        .content(inputString)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputString));
    }

    @Test
    public void deleteArticleTest() throws Exception{
        String inputString = mapper.writeValueAsString(inputArticle);

        doNothing().when(serviceLayer).deleteArticle(inputArticle.getArticleId());

        mockMvc.perform(delete("/article/1")
                        .content(inputString)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturn422ErrorOnInvalidId() throws Exception {

        when(serviceLayer.findArticle(8)).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(get("/article/8")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturn422ErrorForMethodArgumentNotValidException() throws Exception {

        when(serviceLayer.findArticle(1)).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(get("/article/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
    @Test
    public void shouldReturn422DeleteArticle() throws Exception {
        String inputString = mapper.writeValueAsString(inputArticle);

        doThrow(IllegalArgumentException.class).when(serviceLayer).deleteArticle(inputArticle.getArticleId());

        mockMvc.perform(delete("/article/1")
                        .content(inputString)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }


}

