package com.example.commons;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings({"unchecked", "rawtypes"})
public final class PredicateRegistry {
    private static final Map<String, PredicateOperator> MAP;

    static {
        HashMap<String, PredicateOperator> map = new HashMap<>();
        map.put("=", (path, cb, value) -> cb.equal(path, value));
        map.put(">=", (path, cb, value) -> cb.greaterThanOrEqualTo((Path<Comparable>) path, (Comparable) value));
        map.put("<=", (path, cb, value) -> cb.lessThanOrEqualTo((Path<Comparable>) path, (Comparable) value));
        map.put("!=", (path, cb, value) -> cb.notEqual(path, value));
        map.put("~", (path, cb, value) -> cb.like((Expression<String>) path, (String) value));
        MAP = map;
    }

    public static Optional<PredicateOperator> find(String key) {
        return Optional.ofNullable(MAP.get(key));
    }

    public static Set<String> keySet() {
        return MAP.keySet();
    }
}
