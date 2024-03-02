package org.springframework.cloud.gateway.server.mvc.config;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

import java.util.Set;

class Hints implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        MemberCategory[] categories = MemberCategory.values();
        for (Class<?> c : Set.of(
                PredicateProperties.class,
                RouteProperties.class,
                FilterProperties.class))
            hints.reflection().registerType(c, categories);

    }
}