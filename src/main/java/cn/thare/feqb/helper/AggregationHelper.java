package cn.thare.feqb.helper;

import cn.thare.feqb.annotation.func.aggs.CardinalityAggregation;
import cn.thare.feqb.annotation.func.aggs.ExtendedStatsAggregation;
import cn.thare.feqb.annotation.func.aggs.StatsAggregation;
import cn.thare.feqb.annotation.func.aggs.TermsAggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.stats.StatsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStatsAggregationBuilder;
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
        } else if (StatsAggregation.class == annotation.annotationType()) {
            setStatsAggregation(searchSource, (StatsAggregation) annotation, (Boolean) value);
        } else if (ExtendedStatsAggregation.class == annotation.annotationType()) {
            setExtendedStatsAggregation(searchSource, (ExtendedStatsAggregation) annotation, (Boolean) value);
        } else if (CardinalityAggregation.class == annotation.annotationType()) {
            setCardinalityAggregation(searchSource, (CardinalityAggregation) annotation, (Boolean) value);
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

    private static void setStatsAggregation(SearchSourceBuilder searchSource, StatsAggregation aggregation,
            Boolean value) {
        if (!value) {
            return;
        }
        StatsAggregationBuilder statsAggregation = new StatsAggregationBuilder(aggregation.name()).field(aggregation.field());
        searchSource.aggregation(statsAggregation);
    }

    private static void setExtendedStatsAggregation(SearchSourceBuilder searchSource, ExtendedStatsAggregation aggregation,
            Boolean value) {
        if (!value) {
            return;
        }
        ExtendedStatsAggregationBuilder extendedStatsAggregation = new ExtendedStatsAggregationBuilder(aggregation.name()).field(aggregation.field());
        searchSource.aggregation(extendedStatsAggregation);
    }

    private static void setCardinalityAggregation(SearchSourceBuilder searchSource, CardinalityAggregation aggregation,
            Boolean value) {
        if (!value) {
            return;
        }
        CardinalityAggregationBuilder cardinalityAggregation = AggregationBuilders.cardinality(aggregation.name())
                .field(aggregation.field()).precisionThreshold(aggregation.precisionThreshold());
        searchSource.aggregation(cardinalityAggregation);
    }

}
