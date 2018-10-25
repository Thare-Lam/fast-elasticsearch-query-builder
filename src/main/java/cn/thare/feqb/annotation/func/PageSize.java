package cn.thare.feqb.annotation.func;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Field's type must be {@link Integer}
 */
@Page
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PageSize {
}
