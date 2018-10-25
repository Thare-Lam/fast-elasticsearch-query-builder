package cn.thare.feqb.builder;

import cn.thare.feqb.annotation.func.*;
import cn.thare.feqb.annotation.query.Query;
import cn.thare.feqb.helper.*;
import cn.thare.feqb.helper.QueryHelper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Abstract query builder
 * @param <T> search criteria DTO
 */
@Slf4j
public abstract class AbstractQueryBuilder<T> {

    private PageTool pageTool;

    /**
     * Set custom {@link PageTool}
     *
     * @param pageTool custom {@link PageTool}
     */
    public void setPageTool(PageTool pageTool) {
        this.pageTool = pageTool;
    }

    protected AbstractQueryBuilder() {
        this.pageTool = PageTool.defaultPageTool();
    }

    /**
     * Build query string
     *
     * @param t search criteria DTO
     * @return query string
     */
    public String build(T t) {
        SearchSourceBuilder searchSource = new SearchSourceBuilder();
        preBuild(searchSource, t);
        setSearchSource(searchSource, t);
        postBuild(searchSource, t);
        return searchSource.toString();
    }

    private void setSearchSource(SearchSourceBuilder searchSource, T t) {
        PageHelper pageHelper = PageHelper.create();
        QueryHelper queryHelper = QueryHelper.create();

        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            Object value;
            try {
                value = field.get(t);
            } catch (IllegalAccessException ignore) {
                continue;
            }
            if (nullOrEmpty(value)) {
                continue;
            }
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                try {
                    if (Objects.nonNull(annotation.annotationType().getAnnotation(Query.class))) {
                        queryHelper.fill(annotation, field, value);
                        break;
                    } else if (Objects.nonNull(annotation.annotationType().getAnnotation(Page.class))) {
                        pageHelper.fill(annotation, field, value);
                        break;
                    } else if (Objects.nonNull(annotation.annotationType().getAnnotation(Aggregation.class))) {
                        AggregationHelper.set(searchSource, annotation, value);
                        break;
                    } else if (Highlighters.class == annotation.annotationType()) {
                        HighlightHelper.set(searchSource, annotation, value);
                        break;
                    } else if (Sort.class == annotation.annotationType()) {
                        SortHelper.set(searchSource, value);
                        break;
                    } else if (Source.class == annotation.annotationType()) {
                        SourceHelper.set(searchSource, value);
                        break;
                    }
                } catch (Exception e) {
                    log.warn("analyse annotation failed, field: {}, annotation: {}, value:{}, cause: {}",
                            field.getName(), annotation.getClass(), value, e.getStackTrace());
                }
            }
        }

        customQuery(queryHelper, t);
        queryHelper.set(searchSource);
        pageHelper.set(searchSource, pageTool);
    }

    private void customQuery(QueryHelper queryHelper, T t) {
        customMustQueries(queryHelper.mustQueries(), t);
        customMustNotQueries(queryHelper.mustNotQueries(), t);
        customShouldQueries(queryHelper.shouldQueries(), t);
        customFilterQueries(queryHelper.filterQueries(), t);
        customOrQueries(queryHelper.orQueries(), t);
        customOrNotQueries(queryHelper.orNotQueries(), t);
    }

    private boolean nullOrEmpty(Object value) {
        if (Objects.isNull(value)) {
            return true;
        }
        try {
            if (value instanceof Collection) {
                return ((Collection) value).isEmpty();
            } else {
                return value.toString().isEmpty();
            }
        } catch (Exception ignored) {
            return true;
        }
    }

    /**
     * Custom before {@link AbstractQueryBuilder#setSearchSource(SearchSourceBuilder, Object)}
     *
     * @param searchSource {@link SearchSourceBuilder} object
     * @param t            search criteria DTO
     */
    protected abstract void preBuild(SearchSourceBuilder searchSource, T t);

    /**
     * Custom after {@link AbstractQueryBuilder#setSearchSource(SearchSourceBuilder, Object)}
     *
     * @param searchSource {@link SearchSourceBuilder} object
     * @param t            search criteria DTO
     */
    protected abstract void postBuild(SearchSourceBuilder searchSource, T t);

    /**
     * Custom must query
     *
     * @param mustQueries queries in must clause
     * @param t           search criteria DTO
     */
    protected abstract void customMustQueries(List<QueryBuilder> mustQueries, T t);

    /**
     * Custom must_ot query
     *
     * @param mustNotQueries queries in must_not clause
     * @param t              search criteria DTO
     */
    protected abstract void customMustNotQueries(List<QueryBuilder> mustNotQueries, T t);

    /**
     * Custom should query
     *
     * @param shouldQueries queries in must_not clause
     * @param t             search criteria DTO
     */
    protected abstract void customShouldQueries(List<QueryBuilder> shouldQueries, T t);

    /**
     * Custom filter query
     *
     * @param filterQueries queries in filter clause
     * @param t             search criteria DTO
     */
    protected abstract void customFilterQueries(List<QueryBuilder> filterQueries, T t);

    /**
     * Custom "or" query, which will be converted to a bool.should clause under the filter context
     *
     * @param orQueries queries in "or" clause
     * @param t         search criteria DTO
     */
    protected abstract void customOrQueries(List<QueryBuilder> orQueries, T t);

    /**
     * Custom "or not" query, which will be converted to a bool.should.bool.must_not clause under the filter context
     *
     * @param orNotQueries queries in "or not" clause
     * @param t            search criteria DTO
     */
    protected abstract void customOrNotQueries(List<QueryBuilder> orNotQueries, T t);

}
