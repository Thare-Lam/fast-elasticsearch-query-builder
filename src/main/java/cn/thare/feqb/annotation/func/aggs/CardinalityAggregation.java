package cn.thare.feqb.annotation.func.aggs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Field's type must be {@link Boolean}
 * <p>Implied whether handle CardinalityAggregation
 */
@Aggregation
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CardinalityAggregation {

    String name();

    String field();

    long precisionThreshold() default -1L;

}
