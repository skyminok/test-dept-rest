package com.example.dept;

import com.example.commons.CompileException;
import com.example.dept.DepartmentController.DepartmentResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DepartmentControllerTest {

    @Autowired
    private DepartmentController sut;

    @Test
    void testList() throws CompileException {
        ResponseEntity<DepartmentController.DepartmentListResponse> result =
                sut.list(Collections.singletonList("creationDate-"),
                        Collections.singletonList("depName~%test%"), 5, 1);
        commonAsserts(result);
    }

    @Test
    void testNoPaging() throws CompileException {
        ResponseEntity<DepartmentController.DepartmentListResponse> result =
                sut.list(null, null, 25, -1);
        commonAsserts(result);
    }

    @Test
    void testNoPagingWithFilteringAndSorting() throws CompileException {
        ResponseEntity<DepartmentController.DepartmentListResponse> result =
                sut.list(Collections.singletonList("depName"),
                        Collections.singletonList("creationDate>=2020-08-01T00:00:00-0300"), 25, -1);
        commonAsserts(result);
    }

    @Test
    void testFindByIdNoContent() {
        ResponseEntity<DepartmentResponse> responseEntity = sut.findById(UUID.randomUUID());
        assertEquals(204, responseEntity.getStatusCodeValue());
        assertNull(responseEntity.getBody());
    }

    @Disabled
    @Test
    void testFindByIdFound() {
        UUID uuid = UUID.fromString("6dcc7d05-8026-4930-838c-61b8c8f974db");
        ResponseEntity<DepartmentResponse> responseEntity =
                sut.findById(uuid);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        Department payload = responseEntity.getBody().getPayload();
        assertNotNull(payload);
        assertEquals(uuid, payload.getDepartmentUuid());
        assertNotNull(payload.getDepName());
    }

    @Test
    void testCreate() {
        Department request = new Department();
        request.setDepName("test" + UUID.randomUUID().toString());
        request.setQualityCheck(1);
        request.setWhseCheck(0);
        request.setDeleted(0);

        ResponseEntity<DepartmentResponse> responseEntity = sut.create(request);

        assertNotNull(responseEntity);
        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().getResult());
    }

    private void commonAsserts(ResponseEntity<DepartmentController.DepartmentListResponse> entity) {
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertNotNull(entity.getBody());
        assertNotNull(entity.getBody().getPayload());
        assertNotNull(entity.getBody().getResult());
        assertNotNull(entity.getBody().getResult().getResultCode());
    }
}