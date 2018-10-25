package cn.thare.feqb.builder;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;

public class BaseQueryBuilder<T> extends AbstractQueryBuilder<T> {

    /**
     * Custom before {@link AbstractQueryBuilder#setSearchSource(SearchSourceBuilder, Object)}
     *
     * @param searchSource {@link SearchSourceBuilder} object
     * @param t            search criteria DTO
     */
    @Override
    protected void preBuild(SearchSourceBuilder searchSource, T t) {

    }

    /**
     * Custom after {@link AbstractQueryBuilder#setSearchSource(SearchSourceBuilder, Object)}
     *
     * @param searchSource {@link SearchSourceBuilder} object
     * @param t            search criteria DTO
     */
    @Override
    protected void postBuild(SearchSourceBuilder searchSource, T t) {

    }

    /**
     * Custom must query
     *
     * @param mustQueries queries in must clause
     * @param t           search criteria DTO
     */
    @Override
    protected void customMustQueries(List<QueryBuilder> mustQueries, T t) {

    }

    /**
     * Custom must_ot query
     *
     * @param mustNotQueries queries in must_not clause
     * @param t              search criteria DTO
     */
    @Override
    protected void customMustNotQueries(List<QueryBuilder> mustNotQueries, T t) {

    }

    /**
     * Custom should query
     *
     * @param shouldQueries queries in must_not clause
     * @param t             search criteria DTO
     */
    @Override
    protected void customShouldQueries(List<QueryBuilder> shouldQueries, T t) {

    }

    /**
     * Custom filter query
     *
     * @param filterQueries queries in filter clause
     * @param t             search criteria DTO
     */
    @Override
    protected void customFilterQueries(List<QueryBuilder> filterQueries, T t) {

    }

    /**
     * Custom "or" query, which will be converted to a bool.should clause under the filter context
     *
     * @param orQueries queries in "or" clause
     * @param t         search criteria DTO
     */
    @Override
    protected void customOrQueries(List<QueryBuilder> orQueries, T t) {

    }

    /**
     * Custom "or not" query, which will be converted to a bool.should.bool.must_not clause under the filter context
     *
     * @param orNotQueries queries in "or not" clause
     * @param t            search criteria DTO
     */
    @Override
    protected void customOrNotQueries(List<QueryBuilder> orNotQueries, T t) {

    }

}
