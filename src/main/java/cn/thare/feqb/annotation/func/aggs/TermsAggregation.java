package cn.thare.feqb.annotation.func.aggs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Field's type must be {@link Integer}
 * <p>Implied the size of aggregation set, limited by {@link TermsAggregation#maxSize()}
 */
@Aggregation
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TermsAggregation {

    String name();

    String field();

    /**
     * @return the max value of the size of aggregation set
     */
    int maxSize() default 20;

    Order[] order() default {};

    ExecutionHint executionHint() default ExecutionHint.GLOBAL_ORDINALS;

    enum Order {

        COUNT_ASC, COUNT_DESC, KEY_ASC, KEY_DESC

    }

    enum ExecutionHint {

        MAP("map"), GLOBAL_ORDINALS("global_ordinals");

        private String value;

        ExecutionHint(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }

    }


}
