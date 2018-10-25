package cn.thare.feqb.annotation.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Will be converted to a bool.should clause under the filter context
 * At least one clause should be satisfied
 * Mapped to "or" query in sql
 */
@Query
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Or {
}
