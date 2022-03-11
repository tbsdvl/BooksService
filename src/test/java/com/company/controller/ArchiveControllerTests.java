package com.company.controller;

import com.company.exceptions.NotFoundException;
import com.company.model.Archive;
import com.company.model.Article;
import com.company.repository.ArchiveRepository;
import com.company.service.ServiceLayer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ArchiveController.class)
public class ArchiveControllerTests{
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    ServiceLayer serviceLayer;
//    ArchiveRepository archiveRepository;

    private ObjectMapper mapper = new ObjectMapper();
    private Archive inputArchive;
    private Archive outputArchive;
    private Article article;
    private Set<Article> articleSet;
    private List<Archive> archiveList;

    @Before
    public void setUp() throws Exception {
        inputArchive = new Archive();
        inputArchive.setArchiveId(1);
        inputArchive.setArchiveName("software");

        article = new Article();
        article.setArchiveId(1);
        article.setLink("link to article");
        article.setTitle("title of article");
        articleSet = new HashSet<>();
        articleSet.add(article);
        inputArchive.setArticles(articleSet);

        outputArchive = new Archive();
        outputArchive.setArchiveId(1);
        outputArchive.setArchiveName("software");
        outputArchive.setArticles(articleSet);

        archiveList = new ArrayList<>();
        archiveList.add(inputArchive);

    }

    @Test
    public void createArchiveTest() throws Exception{
        String inputString = mapper.writeValueAsString(inputArchive);
        String outputString = mapper.writeValueAsString(outputArchive);

        when(serviceLayer.saveArchive(inputArchive)).thenReturn(outputArchive);

        mockMvc.perform(post("/archive")
                        .content(inputString)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputString));
    }

    @Test
    public void shouldThrow422OnEmptyArchive() throws Exception {
        Archive emptyArchive = new Archive();
        String inputEmptyArchive = mapper.writeValueAsString(emptyArchive);
        when(serviceLayer.saveArchive(emptyArchive)).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(post("/archive")
                        .content(inputEmptyArchive)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void findAllArchivesTest() throws Exception{
        String outputString = mapper.writeValueAsString(archiveList);
        when(serviceLayer.findAllArchives()).thenReturn(archiveList);
        mockMvc.perform(get("/archive")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputString));
    }

    @Test
    public void findArchiveByIdTest() throws Exception{
        String inputString = mapper.writeValueAsString(inputArchive);
        String outputString = mapper.writeValueAsString(outputArchive);

        when(serviceLayer.findArchive(inputArchive.getArchiveId())).thenReturn(Optional.ofNullable(outputArchive));

        mockMvc.perform(get("/archive/1")
                        .content(inputString)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputString));
    }

    @Test
    public void shouldReturn422FindById() throws Exception {

        when(serviceLayer.findArchive(8577)).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(get("/archive/8577")
//                        .content(inputFindArchive)
                                .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

//    @Test
//    public void shouldReturn404FindById() throws Exception {
//        Archive archive = new Archive();
//        archive.setArchiveName("archive name");
//        archive.setArchiveId(111);
//        String inputFindArchive = mapper.writeValueAsString(archive);
//
//        when(serviceLayer.findArchive(archive.getArchiveId())).thenThrow(NotFoundException.class);
//
//        mockMvc.perform(get("/archive/-4")
//                        .content(inputFindArchive)
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().isNotFound());
//    }

    @Test
    public void updateArchiveByIdTest() throws Exception{
        String inputString = mapper.writeValueAsString(inputArchive);
        outputArchive.setArchiveName("changed");
        outputArchive.setArchiveId(2);
        String outputString = mapper.writeValueAsString(outputArchive);

        when(serviceLayer.findArchive(inputArchive.getArchiveId())).thenReturn(Optional.ofNullable(outputArchive));
        when(serviceLayer.updateArchive(inputArchive)).thenReturn(outputArchive);

        mockMvc.perform(put("/archive/1")
                        .content(inputString)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputString));
    }

    /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////
    @Test
    public void shouldReturn422UpdateArchive() throws Exception {
        Archive newArchive = new Archive();
        newArchive.setArchiveId(3);
//        newArchive.setArchiveName("name");
        String inputString = mapper.writeValueAsString(newArchive);

//        when(serviceLayer.findArchive(inputArchive.getArchiveId())).thenReturn(Optional.ofNullable(inputArchive));
        when(serviceLayer.updateArchive(newArchive)).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(put("/archive/3")
                        .content(inputString)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
    /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////

    @Test
    public void deleteArchiveTest() throws Exception {
        String inputString = mapper.writeValueAsString(inputArchive);

        doNothing().when(serviceLayer).deleteArchive(inputArchive.getArchiveId());

        mockMvc.perform(delete("/archive/1")
                        .content(inputString)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturn422DeleteArchive() throws Exception {
        String inputString = mapper.writeValueAsString(inputArchive);

        doThrow(IllegalArgumentException.class).when(serviceLayer).deleteArchive(inputArchive.getArchiveId());

        mockMvc.perform(delete("/archive/1")
                        .content(inputString)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }


}
