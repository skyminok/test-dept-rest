package com.example.org;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class OrganizationControllerMvcTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void testList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/organizations")
                .param("filter", "legalName=asdfddmsasds"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"payload\":{\"data\":[]," +
                        "\"totalPages\":1},\"result\":{\"resultCode\":0,\"resultMessage\":\"OK\"}}"));

    }
}