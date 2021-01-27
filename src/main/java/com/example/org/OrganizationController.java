package com.example.org;

import com.example.commons.BaseResponse;
import com.example.commons.CommonListProvider;
import com.example.commons.CompileException;
import com.example.commons.PagedResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/organizations", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class OrganizationController {

    private final CommonListProvider listProvider;
    private final OrganizationRepository repository;

    /**
     * См. {@link com.example.dept.DepartmentController#list(List, List, Integer, Integer)}
     */
    @GetMapping
    public ResponseEntity<OrganizationListResponse> list(
            @RequestParam(name = "sort", required = false) List<String> sortList,
            @RequestParam(name = "filter", required = false) List<String> filters,
            @RequestParam(name = "limit", defaultValue = "25", required = false) Integer limit,
            @RequestParam(name = "page", defaultValue = "-1", required = false) Integer pageIndex
    ) throws CompileException {
        Page<Organization> page =
                listProvider.query(sortList, filters, limit, pageIndex, Organization.class, repository);
        OrganizationPagedResult pagedResult = new OrganizationPagedResult(page.getContent(), page.getTotalPages());
        OrganizationListResponse response = new OrganizationListResponse(pagedResult);
        return ResponseEntity.ok(response);
    }

    public static class OrganizationPagedResult extends PagedResult<Organization> {
        public OrganizationPagedResult(List<Organization> data, int totalPages) {
            super(data, totalPages);
        }
    }

    public static class OrganizationListResponse extends BaseResponse<OrganizationPagedResult> {
        public OrganizationListResponse(OrganizationPagedResult payload) {
            super(payload);
        }
    }
}
