package cn.thare.feqb.helper;

import cn.thare.feqb.annotation.query.*;
import cn.thare.feqb.annotation.query.type.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Slf4j
public class QueryHelper {

    private final static List<Class> OCCUR_TYPES = Arrays.asList(Must.class, MustNot.class, Should.class, Filter.class,
            Or.class, OrNot.class);

    private Map<Class, List<QueryBuilder>> queryMap;

    private QueryHelper() {
        queryMap = new HashMap<>();
        OCCUR_TYPES.forEach(t -> queryMap.put(t, new ArrayList<>()));
    }

    public static QueryHelper create() {
        return new QueryHelper();
    }

    public void fill(Annotation annotation, Field field, Object value) {
        Optional.ofNullable(getQuery(field, value))
                .ifPresent(t -> queryMap.get(annotation.annotationType()).add(t));
    }

    public void set(SearchSourceBuilder searchSource) {
        BoolQueryBuilder boolQuery = boolQuery();

        if (orQueries().size() > 0 || orNotQueries().size() > 0) {
            BoolQueryBuilder orQuery = boolQuery();
            orQueries().forEach(orQuery::should);
            orNotQueries().forEach(t -> orQuery.should(boolQuery().mustNot(t)));
            filterQueries().add(orQuery);
        }

        mustQueries().forEach(boolQuery::must);
        mustNotQueries().forEach(boolQuery::mustNot);
        shouldQueries().forEach(boolQuery::should);
        filterQueries().forEach(boolQuery::filter);

        searchSource.query(boolQuery);
    }

    public List<QueryBuilder> mustQueries() {
        return queryMap.get(Must.class);
    }

    public List<QueryBuilder> mustNotQueries() {
        return queryMap.get(MustNot.class);
    }

    public List<QueryBuilder> shouldQueries() {
        return queryMap.get(Should.class);
    }

    public List<QueryBuilder> filterQueries() {
        return queryMap.get(Filter.class);
    }

    public List<QueryBuilder> orQueries() {
        return queryMap.get(Or.class);
    }

    public List<QueryBuilder> orNotQueries() {
        return queryMap.get(OrNot.class);
    }

    private QueryBuilder getQuery(Field field, Object value) {
        try {
            if (Objects.nonNull(field.getDeclaredAnnotation(Match.class))) {
                Match match = field.getDeclaredAnnotation(Match.class);
                String fieldName = firstHasText(match.fieldName(), field.getName());
                return matchQuery(fieldName, value).operator(Operator.fromString(match.operator().value()));
            } else if (Objects.nonNull(field.getDeclaredAnnotation(MatchPhrase.class))) {
                MatchPhrase matchPhrase = field.getDeclaredAnnotation(MatchPhrase.class);
                String fieldName = firstHasText(matchPhrase.fieldName(), field.getName());
                MatchPhraseQueryBuilder query = matchPhraseQuery(fieldName, value);
                String analyzer = matchPhrase.analyzer().trim();
                if (analyzer.length() > 0) {
                    query.analyzer(analyzer);
                }
                int slop = matchPhrase.slop();
                if (slop != -1) {
                    query.slop(slop);
                }
                return query;
            } else if (Objects.nonNull(field.getDeclaredAnnotation(Term.class))) {
                Term term = field.getDeclaredAnnotation(Term.class);
                String fieldName = firstHasText(term.fieldName(), field.getName());
                return termQuery(fieldName, value);
            } else if (Objects.nonNull(field.getDeclaredAnnotation(Terms.class))) {
                Terms terms = field.getDeclaredAnnotation(Terms.class);
                String fieldName = firstHasText(terms.fieldName(), field.getName());
                return termsQuery(fieldName, (Collection) value);
            } else if (Objects.nonNull(field.getDeclaredAnnotation(Wildcard.class))) {
                Wildcard wildcard = field.getDeclaredAnnotation(Wildcard.class);
                String fieldName = firstHasText(wildcard.fieldName(), field.getName());
                return wildcardQuery(fieldName, value.toString());
            } else if (Objects.nonNull(field.getDeclaredAnnotation(Exists.class))) {
                Exists exists = field.getDeclaredAnnotation(Exists.class);
                String fieldName = firstHasText(exists.fieldName(), field.getName());
                QueryBuilder query = existsQuery(fieldName);
                return (Boolean) value ? query : boolQuery().mustNot(query);
            } else if (Objects.nonNull(field.getDeclaredAnnotation(Range.class))) {
                Range range = field.getDeclaredAnnotation(Range.class);
                String fieldName = firstHasText(range.fieldName(), field.getName());
                RangeQueryBuilder rangeQuery = rangeQuery(fieldName);
                switch (range.type()) {
                    case FROM:
                        rangeQuery.from(value, range.includedBoundary());
                        break;
                    case TO:
                        rangeQuery.to(value, range.includedBoundary());
                        break;
                    default:
                        break;
                }
                return rangeQuery;
            }
        } catch (Exception e) {
            log.warn("getQuery failed, field: {}, value: {}, cause: {}", field, value, e.getStackTrace());
        }
        return null;
    }

    private String firstHasText(String first, String second) {
        return (Objects.nonNull(first) && !first.trim().isEmpty()) ? first.trim() : second;
    }

}
