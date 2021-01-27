package com.example.commons;

import com.example.dept.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class CommonListProvider {

    private final ParameterCompiler parameterCompiler;
    private final SortCompiler sortCompiler;

    public <T> Page<T> query(List<String> sortList,
                             List<String> filters,
                             Integer limit,
                             Integer pageIndex,
                             Class<T> entityClass,
                             JpaSpecificationExecutor<T> repository) throws CompileException {
        Specification<T> specification = parameterCompiler.compile(entityClass, filters);
        Sort sort = sortCompiler.compile(Department.class, sortList);
        Page<T> page;
        if (pageIndex >= 0) {
            Pageable pageable = PageRequest.of(pageIndex, limit, sort);
            page = repository.findAll(specification, pageable);
        } else {
            List<T> all = repository.findAll(specification, sort);
            page = new PageImpl<>(all);
        }
        return page;
    }

}
