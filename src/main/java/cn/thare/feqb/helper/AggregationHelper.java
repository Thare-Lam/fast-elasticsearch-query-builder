package cn.thare.feqb.helper;

import cn.thare.feqb.annotation.func.aggs.TermsAggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AggregationHelper {

    public static void set(SearchSourceBuilder searchSource, Annotation annotation, Object value) {
        if (TermsAggregation.class == annotation.annotationType()) {
            setTermsAggregation(searchSource, (TermsAggregation) annotation, (Integer) value);
        }
    }

    private static void setTermsAggregation(SearchSourceBuilder searchSource, TermsAggregation aggregation, Integer size) {
        TermsAggregationBuilder termsAggregation = AggregationBuilders.terms(aggregation.name())
                .field(aggregation.field());
        if (size > 0) {
            termsAggregation.size(Integer.min(size, aggregation.maxSize()));
        }
        if (aggregation.order().length > 0) {
            List<BucketOrder> orders = Stream.of(aggregation.order()).distinct().map(t -> {
                switch (t) {
                    case COUNT_ASC:
                        return BucketOrder.count(Boolean.TRUE);
                    case COUNT_DESC:
                        return BucketOrder.count(Boolean.FALSE);
                    case KEY_ASC:
                        return BucketOrder.key(Boolean.TRUE);
                    case KEY_DESC:
                        return BucketOrder.key(Boolean.FALSE);
                    default:
                        return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
            termsAggregation.order(orders);
        }
        termsAggregation.executionHint(aggregation.executionHint().value());
        searchSource.aggregation(termsAggregation);
    }

}
