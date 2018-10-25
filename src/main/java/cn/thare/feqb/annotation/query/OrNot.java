package cn.thare.feqb.annotation.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Will be converted to a bool.should.bool.must_not clause under the Filter context
 * At least one clause should be satisfied
 * Mapped to "or not" query in sql
 */
@Query
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OrNot {
}
