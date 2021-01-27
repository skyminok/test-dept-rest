package com.example.dept;

import com.example.commons.BaseResponse;
import com.example.commons.CommonListProvider;
import com.example.commons.CompileException;
import com.example.commons.PagedResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping(path = "/departments", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Transactional
public class DepartmentController {

    private final CommonListProvider listProvider;
    private final DepartmentRepository repository;

    /**
     * Данный ресурс является продуктом давних размышлений об обобщенном поставщике данных для гридов
     *
     * @param sortList  список полей сортировки в формате &lt;имя_поля_сущности&gt;[+|-],
     *                  где "+" для ASC, "-" для DESC, по умолчанию ASC
     * @param filters   фильтры в формате &lt;имя_поля_сущности&gt;&lt;оператор>[аргумент].
     *                  Реализованы следующие операторы: =, &gt;=, &lt;=, !=, ~&gt;
     * @param limit     размер страницы или -1, если требуются все элементы
     * @param pageIndex индекс страницы выборки
     * @return объект-обертка для списка объектов типа "Подразделение"
     * @throws CompileException некорректный запрос от клиента
     */
    @GetMapping
    public ResponseEntity<DepartmentListResponse> list(
            @RequestParam(name = "sort", required = false) List<String> sortList,
            @RequestParam(name = "filter", required = false) List<String> filters,
            @RequestParam(name = "limit", defaultValue = "25", required = false) Integer limit,
            @RequestParam(name = "page", defaultValue = "-1", required = false) Integer pageIndex) throws CompileException {

        Page<Department> page =
                listProvider.query(sortList, filters, limit, pageIndex, Department.class, repository);
        DepartmentPagedResult pagedResult = new DepartmentPagedResult(page.getContent(), page.getTotalPages());
        DepartmentListResponse response = new DepartmentListResponse(pagedResult);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<DepartmentResponse> findById(@PathVariable("id") UUID uuid) {
        return repository.findById(uuid)
                .map(department -> ResponseEntity.ok(new DepartmentResponse(department)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DepartmentResponse> create(@RequestBody @NotNull @Valid Department department) {
        Department loaded = doCreateOrUpdate(null, department);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new DepartmentResponse(loaded));
    }

    @PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DepartmentResponse> update(@PathVariable("id") UUID uuid,
                                                     @RequestBody @NotNull @Valid Department department) {
        Department loaded = doCreateOrUpdate(uuid, department);
        return ResponseEntity.ok(new DepartmentResponse(loaded));
    }

    private Department doCreateOrUpdate(UUID uuid, Department department) {
        uuid = repository.addDepartment(uuid,
                department.getDepName(),
                department.getOrganizationUuid(),
                department.getParentDepartmentUuid(),
                department.getChiefPersonUuid(),
                department.getDeleted(),
                department.getQualityCheck(),
                department.getWhseCheck(),
                department.getIsWorkCenter()
        );

        return repository.findById(uuid)
                .orElseThrow();
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID uuid) {
        String uuidReturned = repository.deleteDepartment(uuid);
        if (uuidReturned == null) {
            return ResponseEntity.ok(new DepartmentResponse(-1, "Департамент не найден"));
        }
        return ResponseEntity.noContent()
                .build();
    }


    public static class DepartmentPagedResult extends PagedResult<Department> {

        public DepartmentPagedResult(List<Department> data, int totalPages) {
            super(data, totalPages);
        }
    }

    public static class DepartmentListResponse extends BaseResponse<DepartmentPagedResult> {
        public DepartmentListResponse(DepartmentPagedResult payload) {
            super(payload);
        }
    }

    public static class DepartmentResponse extends BaseResponse<Department> {

        public DepartmentResponse(Department payload) {
            super(payload);
        }

        public DepartmentResponse(Integer code, String message) {
            super(code, message);
        }
    }

}
