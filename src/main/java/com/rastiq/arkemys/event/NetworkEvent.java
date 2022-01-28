package com.rastiq.arkemys.event;

import java.lang.annotation.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NetworkEvent {
}
