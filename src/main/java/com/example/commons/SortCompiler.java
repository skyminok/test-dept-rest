package com.example.commons;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.EntityType;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class SortCompiler {
    private static final Pattern PATTERN = Pattern.compile("([a-zA-Z_0-9]*)([+\\-])?");

    private final EntityManagerFactory emf;

    public Sort compile(Class<?> clazz, List<String> sortList) throws CompileException {
        if (sortList == null || sortList.isEmpty()) {
            return Sort.unsorted();
        }
        EntityType<?> entityType = emf.getMetamodel().entity(clazz);

        List<Order> list = new LinkedList<>();
        for (String sortExpression : sortList) {
            Matcher matcher = PATTERN.matcher(sortExpression);
            if (!matcher.matches()) {
                throw new CompileException("Wrong sort expression: " + sortExpression);
            }
            String attribute = matcher.group(1);

            checkAttribute(entityType, attribute);

            String directionSign = matcher.group(2);
            Sort.Direction direction = "-".equals(directionSign) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Order order = new Order(direction, attribute);
            list.add(order);
        }
        return Sort.by(list);
    }

    private void checkAttribute(EntityType<?> entityType, String attribute) throws CompileException {
        try {
            entityType.getSingularAttribute(attribute);
        } catch (IllegalArgumentException e) {
            throw new CompileException("Unknown sort attribute: " + attribute);
        }
    }
}
