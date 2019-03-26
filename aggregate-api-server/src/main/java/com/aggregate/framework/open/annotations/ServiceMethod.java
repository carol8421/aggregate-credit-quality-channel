package com.aggregate.framework.open.annotations;

import org.hibernate.validator.constraints.NotBlank;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceMethod {

    @NotBlank
    String methodName();
}
