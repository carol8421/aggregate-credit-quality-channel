package com.aggregate.framework.open.annotations;

import org.hibernate.validator.constraints.NotBlank;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceChannel {

    @NotBlank
    String channelName();
}
