package com.example.commons;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ParameterCompiler {

    private final ConversionService conversionService;
    private final EntityManagerFactory emf;

    public <T> Specification<T> compile(Class<T> entityClass, List<String> parametersAsString) throws CompileException {
        if (parametersAsString == null || parametersAsString.isEmpty()) {
            return (root, query, cb) ->
                    cb.equal(cb.literal(1), cb.literal(1));
        }
        List<RequestParam> list = new LinkedList<>();
        for (String filterExpression : parametersAsString) {
            try {
                FilterParser.ParsedFilter parsedFilter = FilterParser.parse(filterExpression);
                SingularAttribute<?, ?> attribute = getAttribute(entityClass, parsedFilter);
                Object convertedArgument = convertParameter(parsedFilter, attribute);
                PredicateOperator operator = getOperator(filterExpression, parsedFilter);
                RequestParam requestParam = new RequestParam(attribute, operator, convertedArgument);
                list.add(requestParam);
            } catch (ParseException e) {
                throw new CompileException("Cannot parse filter expression: " + filterExpression, e);
            }
        }
        return (root, query, criteriaBuilder) -> {
            Predicate[] predicates = list.stream()
                    .map(param -> param.toPredicate(root, criteriaBuilder))
                    .toArray(Predicate[]::new);
            return criteriaBuilder.and(predicates);
        };
    }

    private Object convertParameter(FilterParser.ParsedFilter parsedFilter, SingularAttribute<?, ?> attribute) throws CompileException {
        try {
            return conversionService.convert(parsedFilter.getArgument(), attribute.getJavaType());
        } catch (ConversionFailedException e) {
            throw new CompileException("Wrong parameter format of attribute '" + parsedFilter.getAttributeName()
                    + "': " + parsedFilter.getArgument());
        }
    }

    private PredicateOperator getOperator(String filterExpression, FilterParser.ParsedFilter parsedFilter) throws CompileException {
        return PredicateRegistry.find(parsedFilter.getOperator())
                .orElseThrow(() -> new CompileException("Unknown operator '" + parsedFilter.getOperator() +
                        "' in expression: " + filterExpression));
    }

    private <T> SingularAttribute<?, ?> getAttribute(Class<T> entityClass, FilterParser.ParsedFilter parsedFilter) throws CompileException {
        EntityType<T> entityType = emf.getMetamodel().entity(entityClass);
        SingularAttribute<?, ?> attribute;
        try {
            attribute = entityType.getSingularAttribute(parsedFilter.getAttributeName());
        } catch (IllegalArgumentException e) {
            throw new CompileException("Unknown attribute: " + parsedFilter.getAttributeName());
        }
        return attribute;
    }

    @RequiredArgsConstructor
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static class RequestParam {
        private final SingularAttribute attribute;
        private final PredicateOperator operation;
        private final Object filterValue;

        public Predicate toPredicate(Root<?> root, CriteriaBuilder cb) {
            Path<?> path = root.get(attribute);
            return operation.apply(path, cb, filterValue);
        }
    }
}
