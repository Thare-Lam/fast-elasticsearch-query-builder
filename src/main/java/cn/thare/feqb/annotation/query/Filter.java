package cn.thare.feqb.annotation.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Query
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Filter {
}
