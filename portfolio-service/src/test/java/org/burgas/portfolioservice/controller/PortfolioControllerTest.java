package org.burgas.portfolioservice.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.burgas.portfolioservice.entity.Identity;
import org.burgas.portfolioservice.entity.Portfolio;
import org.burgas.portfolioservice.entity.Profession;
import org.burgas.portfolioservice.exception.IdentityNotFoundException;
import org.burgas.portfolioservice.exception.PortfolioNotFoundException;
import org.burgas.portfolioservice.exception.ProfessionNotFoundException;
import org.burgas.portfolioservice.message.IdentityMessages;
import org.burgas.portfolioservice.message.PortfolioMessages;
import org.burgas.portfolioservice.message.ProfessionMessages;
import org.burgas.portfolioservice.repository.IdentityRepository;
import org.burgas.portfolioservice.repository.PortfolioRepository;
import org.burgas.portfolioservice.repository.ProfessionRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class PortfolioControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PortfolioRepository portfolioRepository;

    @Autowired
    IdentityRepository identityRepository;

    @Autowired
    ProfessionRepository professionRepository;

    @Test
    @SneakyThrows
    @Order(value = 3)
    void getAllPortfolios() {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/portfolios")
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
    @Order(value = 4)
    void getPortfolioById() {
        Portfolio portfolio = this.portfolioRepository.findPortfolioByName("New Portfolio for GO developer")
                .orElseThrow(() -> new PortfolioNotFoundException(PortfolioMessages.PORTFOLIO_NOT_FOUND.getMessage()));
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/portfolios/by-id")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("portfolioId", portfolio.getId().toString())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @SneakyThrows
    @Order(value = 1)
    @WithUserDetails(value = "burgasvv@gmail.com", userDetailsServiceBeanName = "userDetailsServiceImpl", setupBefore = TestExecutionEvent.TEST_METHOD)
    void createPortfolio() {
        Identity identity = this.identityRepository.findIdentityByEmail("burgasvv@gmail.com")
                .orElseThrow(() -> new IdentityNotFoundException(IdentityMessages.IDENTITY_NOT_FOUND.getMessage()));
        Profession profession = this.professionRepository.findProfessionByName("Go разработчик")
                .orElseThrow(() -> new ProfessionNotFoundException(ProfessionMessages.PROFESSION_NOT_FOUND.getMessage()));
        String content = """
                {
                    "name": "New Portfolio for GO developer",
                    "description": "New Description for portfolio",
                    "identityId": "%s",
                    "professionId": "%s",
                    "opened": true
                }
                """
                .formatted(identity.getId().toString(), profession.getId().toString());
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/api/v1/portfolios/create")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .param("identityId", identity.getId().toString())
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andExpect(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @SneakyThrows
    @Order(value = 2)
    @WithUserDetails(value = "burgasvv@gmail.com", userDetailsServiceBeanName = "userDetailsServiceImpl", setupBefore = TestExecutionEvent.TEST_METHOD)
    void updatePortfolio() {
        Identity identity = this.identityRepository.findIdentityByEmail("burgasvv@gmail.com")
                .orElseThrow(() -> new IdentityNotFoundException(IdentityMessages.IDENTITY_NOT_FOUND.getMessage()));
        Portfolio portfolio = this.portfolioRepository.findPortfolioByName("New Portfolio for GO developer")
                .orElseThrow(() -> new PortfolioNotFoundException(PortfolioMessages.PORTFOLIO_NOT_FOUND.getMessage()));
        String content = """
                {
                    "id": "%s",
                    "opened": false
                }
                """
                .formatted(portfolio.getId().toString());
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/api/v1/portfolios/update")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .param("identityId", identity.getId().toString())
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
    @WithUserDetails(value = "burgasvv@gmail.com", userDetailsServiceBeanName = "userDetailsServiceImpl", setupBefore = TestExecutionEvent.TEST_METHOD)
    void deletePortfolio() {
        Identity identity = this.identityRepository.findIdentityByEmail("burgasvv@gmail.com")
                .orElseThrow(() -> new IdentityNotFoundException(IdentityMessages.IDENTITY_NOT_FOUND.getMessage()));
        Portfolio portfolio = this.portfolioRepository.findPortfolioByName("New Portfolio for GO developer")
                .orElseThrow(() -> new PortfolioNotFoundException(PortfolioMessages.PORTFOLIO_NOT_FOUND.getMessage()));
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete("/api/v1/portfolios/delete")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .param("identityId", identity.getId().toString())
                                .param("portfolioId", portfolio.getId().toString())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }
}