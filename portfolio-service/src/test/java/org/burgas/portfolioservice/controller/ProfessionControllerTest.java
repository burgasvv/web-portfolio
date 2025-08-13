package org.burgas.portfolioservice.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.burgas.portfolioservice.entity.Profession;
import org.burgas.portfolioservice.exception.ProfessionNotFoundException;
import org.burgas.portfolioservice.message.ProfessionMessages;
import org.burgas.portfolioservice.repository.ProfessionRepository;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class ProfessionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProfessionRepository professionRepository;

    @Test
    @SneakyThrows
    @Order(value = 1)
    void getAllProfessions() {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/professions"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @SneakyThrows
    @Order(value = 2)
    void getProfessionById() {
        Profession profession = this.professionRepository.findProfessionByName("Java разработчик")
                .orElseThrow(() -> new ProfessionNotFoundException(ProfessionMessages.PROFESSION_NOT_FOUND.getMessage()));
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/professions/by-id")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .param("professionId", profession.getId().toString())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @SneakyThrows
    @Order(value = 3)
    @WithMockUser(value = "burgasvv", username = "burgasvv@gmail.com", password = "burgasvv", authorities = "ADMIN")
    void createProfession() {
        @Language("JSON") String content = """
                {
                    "name": "New Profession",
                    "description": "New description for New Profession"
                }""";
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/api/v1/professions/create")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @SneakyThrows
    @Order(value = 4)
    @WithMockUser(value = "burgasvv", username = "burgasvv@gmail.com", password = "burgasvv", authorities = "ADMIN")
    void updateProfession() {
        Profession profession = this.professionRepository.findProfessionByName("New Profession")
                .orElseThrow(() -> new ProfessionNotFoundException(ProfessionMessages.PROFESSION_NOT_FOUND.getMessage()));
        @Language("JSON") String content = """
                {
                    "id": "%s",
                    "name": "New Profession UPDATED"
                }"""
                .formatted(profession.getId().toString());
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/api/v1/professions/update")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .content(content)
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @SneakyThrows
    @Order(value = 5)
    @WithMockUser(value = "burgasvv", username = "burgasvv@gmail.com", password = "burgasvv", authorities = "ADMIN")
    void deleteProfession() {
        Profession profession = this.professionRepository.findProfessionByName("New Profession UPDATED")
                .orElseThrow(() -> new ProfessionNotFoundException(ProfessionMessages.PROFESSION_NOT_FOUND.getMessage()));
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete("/api/v1/professions/delete")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .param("professionId", profession.getId().toString())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }
}