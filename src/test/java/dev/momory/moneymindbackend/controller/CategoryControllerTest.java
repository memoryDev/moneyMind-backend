package dev.momory.moneymindbackend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.momory.moneymindbackend.dto.PageResponseDTO;
import dev.momory.moneymindbackend.entity.Category;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private CategoryController categoryController;

    @Autowired
    private MockMvc mockMvc;

    private String jwtToken;

    @BeforeEach
    void setUp() throws Exception {
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/login")
                                .param("userid", "user1")
                                .param("password", "user1")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        jwtToken = result.getResponse().getHeader("access");
    }

    @Test
    void getCategoriesTests() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/categories")
                        .header("access", jwtToken)
                        .param("page", "0")
                        .param("searchType", "ALL")
                        .param("searchValue", "")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();



    }



}