package cn.thare.feqb.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;

@Slf4j
public class TestQueryBuilder {

    @Test
    public void testBuildQuery() {
        StuQueryBuilder queryBuilder = new StuQueryBuilder();
        StuSearchCriteria searchCriteria = StuSearchCriteria.builder()
                .pageNo(3)
                .pageSize(15)
                .highlighters(Arrays.asList(StuFieldName.INTRODUCE))
                .sources(Arrays.asList(StuFieldName.NAME, StuFieldName.AGE, StuFieldName.MATH_SCORE))
                .ageAggregation(2)
                .sorts(Arrays.asList(StuSortEnum.MATH_SCORE_DESC, StuSortEnum.SCORE_DESC))
                .school("USTC")
                .tags(Arrays.asList("basketball", "swimming"))
                .existInternship(Boolean.TRUE)
                .mathScoreFrom(80)
                .name("ja*")
                .excludedAddress("Beijing")
                .introduce("java python")
                .customField("customFieldValue")
                .build();
        log.info("dsl: \n{}", queryBuilder.build(searchCriteria));
    }

}
