package cn.thare.feqb.annotation.query.type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Match {

    /**
     * Query's field name
     *
     * @return query's field name. If return "", the name will be the DTO 's field name.
     */
    String fieldName() default "";

    Operator operator() default Operator.OR;

    enum Operator {
        AND("AND"), OR("OR");

        private String value;

        Operator(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }

}
