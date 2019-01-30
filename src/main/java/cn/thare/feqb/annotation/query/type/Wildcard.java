package cn.thare.feqb.annotation.query.type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Wildcard {

    /**
     * Query's field name
     *
     * @return query's field name. If return "", the name will be the DTO 's field name.
     */
    String fieldName() default "";

}
