package net.kuryshev.controller.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by 1 on 27.06.2017.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
    String value();
}
