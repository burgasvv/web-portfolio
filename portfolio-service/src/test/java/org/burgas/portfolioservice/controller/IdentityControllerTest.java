package org.burgas.portfolioservice.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.burgas.portfolioservice.entity.Identity;
import org.burgas.portfolioservice.exception.IdentityNotFoundException;
import org.burgas.portfolioservice.message.IdentityMessages;
import org.burgas.portfolioservice.repository.IdentityRepository;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class IdentityControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    IdentityRepository identityRepository;

    @Test
    @SneakyThrows
    @Order(value = 1)
    @WithMockUser(value = "burgasvv", username = "burgasvv@gmail.com", password = "burgasvv", authorities = "ADMIN")
    void getAllIdentities() {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/identities"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @SneakyThrows
    @Order(value = 2)
    @WithUserDetails(value = "burgasvv@gmail.com", userDetailsServiceBeanName = "userDetailsServiceImpl", setupBefore = TestExecutionEvent.TEST_METHOD)
    void getIdentityById() {
        Identity identity = this.identityRepository.findIdentityByEmail("burgasvv@gmail.com")
                .orElseThrow(() -> new IdentityNotFoundException(IdentityMessages.IDENTITY_NOT_FOUND.getMessage()));
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/identities/by-id")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .param("identityId", identity.getId().toString())
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
    void createIdentity() {
        @Language("JSON") String content = """
                {
                    "username": "user",
                    "password": "user",
                    "email": "user@gmail.com",
                    "phone": "+7(948)-395-68-39"
                }""";
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/api/v1/identities/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(content)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
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
    @WithUserDetails(value = "user@gmail.com", userDetailsServiceBeanName = "userDetailsServiceImpl", setupBefore = TestExecutionEvent.TEST_METHOD)
    void updateIdentity() {
        Identity identity = this.identityRepository.findIdentityByEmail("user@gmail.com")
                .orElseThrow(() -> new IdentityNotFoundException(IdentityMessages.IDENTITY_NOT_FOUND.getMessage()));
        @Language("JSON") String content = """
                {
                    "phone": "+7(913)-465-00-19"
                }""";
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put("/api/v1/identities/update")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .param("identityId", identity.getId().toString())
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @SneakyThrows
    @Order(value = 5)
    @WithUserDetails(value = "user@gmail.com", userDetailsServiceBeanName = "userDetailsServiceImpl", setupBefore = TestExecutionEvent.TEST_METHOD)
    void deleteIdentity() {
        Identity identity = this.identityRepository.findIdentityByEmail("user@gmail.com")
                .orElseThrow(() -> new IdentityNotFoundException(IdentityMessages.IDENTITY_NOT_FOUND.getMessage()));
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete("/api/v1/identities/delete")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .param("identityId", identity.getId().toString())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }
}