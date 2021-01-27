package com.example.commons;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

public interface PredicateOperator {

    Predicate apply(Path<?> path, CriteriaBuilder cb, Object value);
}
