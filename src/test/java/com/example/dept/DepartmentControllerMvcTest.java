package com.example.dept;

import com.example.dept.DepartmentController.DepartmentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
class DepartmentControllerMvcTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListEmpty() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/departments")
                .param("filter", "depName=asdfddmsasds"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"payload\":{\"data\":[]," +
                        "\"totalPages\":1},\"result\":{\"resultCode\":0,\"resultMessage\":\"OK\"}}"));
    }

    @Test
    void testListIncorrectDateFormat() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/departments")
                .param("filter", "creationDate>=2020-08-01"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testCreateSuccess() throws Exception {
        create();
    }

    @Test
    void testCreateValidationFail() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/departments")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testGet() throws Exception {
        Department department = create();

        String contentAsString = mvc.perform(
                MockMvcRequestBuilders.get("/departments/" + department.getDepartmentUuid()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString();
        System.out.println(contentAsString);
    }

    @Test
    void testUpdateSuccess() throws Exception {
        Department department = create();

        mvc.perform(MockMvcRequestBuilders.put("/departments/" + department.getDepartmentUuid())
                .content("{\n" +
                        "  \"departmentUuid\": \"" + department.getDepartmentUuid() + "\",\n" +
                        "  \"depName\": \"test-it\",\n" +
                        "  \"parentDepartmentUuid\": null,\n" +
                        "  \"parentDepartmentName\": null,\n" +
                        "  \"chiefPersonUuid\": null,\n" +
                        "  \"chiefPersonName\": \"\",\n" +
                        "  \"isWorkCenter\": 0,\n" +
                        "  \"qualityCheck\": 0,\n" +
                        "  \"whseCheck\": 0,\n" +
                        "  \"creationDate\": \"2020-11-24T02:06:38+0300\",\n" +
                        "  \"deleted\": 0\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDelete() throws Exception {
        Department department = create();
        mvc.perform(MockMvcRequestBuilders.delete("/departments/" + department.getDepartmentUuid()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    private Department create() throws Exception {
        String json = mvc.perform(MockMvcRequestBuilders.post("/departments")
                .content("{\n" +
                        "  \"depName\": \"test-it\",\n" +
                        "  \"qualityCheck\": 1,\n" +
                        "  \"whseCheck\": 0,\n" +
                        "  \"deleted\": 0\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("payload.departmentUuid").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("payload.depName").value("test-it"))
                .andExpect(MockMvcResultMatchers.jsonPath("payload.qualityCheck").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("payload.whseCheck").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("payload.deleted").value(0))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(json, DepartmentResponse.class).getPayload();
    }

}