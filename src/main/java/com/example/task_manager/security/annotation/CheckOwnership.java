package com.example.task_manager.security.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckOwnership {
    // The type of entity being checked
    EntityType entity();

    // The parameter name that contains the entity ID in the method
    String idParam() default "id";
}

