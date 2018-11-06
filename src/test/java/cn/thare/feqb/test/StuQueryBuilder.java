package cn.thare.feqb.test;

import cn.thare.feqb.builder.BaseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

public class StuQueryBuilder extends BaseQueryBuilder<StuSearchCriteria> {

    @Override
    protected void preBuild(SearchSourceBuilder searchSource, StuSearchCriteria stuSearchCriteria) {
        // rewrite search criteria
        stuSearchCriteria.setCustomField("customPrefix-" + stuSearchCriteria.getCustomField() + "-customSuffix");
    }

    @Override
    protected void postBuild(SearchSourceBuilder searchSource, StuSearchCriteria stuSearchCriteria) {
        // special login
        if (stuSearchCriteria.getPageSize() == 1) {
            searchSource.size(10);
        }
    }

    @Override
    protected void customMustQueries(List<QueryBuilder> mustQueries, StuSearchCriteria stuSearchCriteria) {
        Optional.ofNullable(stuSearchCriteria.getCustomField())
                .ifPresent(t -> mustQueries.add(termQuery("customMustField", t)));
    }

    @Override
    protected void customMustNotQueries(List<QueryBuilder> mustNotQueries,
            StuSearchCriteria stuSearchCriteria) {
        Optional.ofNullable(stuSearchCriteria.getCustomField())
                .ifPresent(t -> mustNotQueries.add(termQuery("customMustNotField", t)));
    }

    @Override
    protected void customShouldQueries(List<QueryBuilder> shouldQueries,
            StuSearchCriteria stuSearchCriteria) {
        Optional.ofNullable(stuSearchCriteria.getCustomField())
                .ifPresent(t -> shouldQueries.add(termQuery("customShouldField", t)));
    }

    @Override
    protected void customFilterQueries(List<QueryBuilder> filterQueries,
            StuSearchCriteria stuSearchCriteria) {
        Optional.ofNullable(stuSearchCriteria.getCustomField())
                .ifPresent(t -> filterQueries.add(termQuery("customFilterField", t)));
    }

    @Override
    protected void customOrQueries(List<QueryBuilder> orQueries, StuSearchCriteria stuSearchCriteria) {
        Optional.ofNullable(stuSearchCriteria.getCustomField())
                .ifPresent(t -> orQueries.add(termQuery("customOrField", t)));
    }

    @Override
    protected void customOrNotQueries(List<QueryBuilder> orNotQueries, StuSearchCriteria stuSearchCriteria) {
        Optional.ofNullable(stuSearchCriteria.getCustomField())
                .ifPresent(t -> orNotQueries.add(termQuery("customOrNotField", t)));
    }

}
