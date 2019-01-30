package cn.thare.feqb.test;

import cn.thare.feqb.annotation.func.*;
import cn.thare.feqb.annotation.func.aggs.CardinalityAggregation;
import cn.thare.feqb.annotation.func.aggs.ExtendedStatsAggregation;
import cn.thare.feqb.annotation.func.aggs.StatsAggregation;
import cn.thare.feqb.annotation.func.aggs.TermsAggregation;
import cn.thare.feqb.annotation.query.*;
import cn.thare.feqb.annotation.query.type.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class StuSearchCriteria {

    // func

    @PageNo
    private Integer pageNo;

    @PageSize
    private Integer pageSize;

    @Highlighters
    private List<String> highlighters;

    @Source
    private List<String> sources;

    @Sort
    private List<StuSortEnum> sorts;

    // func - aggs

    @CardinalityAggregation(name = "ageCardinalityAggregation", field = StuFieldName.AGE)
    private Boolean ageCardinalityAggregation;

    @ExtendedStatsAggregation(name = "ageExtendedStatsAggregation", field = StuFieldName.AGE)
    private Boolean ageExtendedStatsAggregation;

    @StatsAggregation(name = "ageStatsAggregation", field = StuFieldName.AGE)
    private Boolean ageStatsAggregation;

    @TermsAggregation(name = "ageTermsAggregation", field = StuFieldName.AGE, maxSize = 5, order = TermsAggregation.Order.KEY_ASC)
    private Integer ageTermsAggregation;

    // query

    @Filter
    @Term
    private String school;

    @Filter
    @Terms(fieldName = StuFieldName.TAG)
    private List<String> tags;

    @Filter
    @Exists(fieldName = StuFieldName.INTERNSHIP)
    private Boolean existInternship;

    @Filter
    @Range(fieldName = StuFieldName.MATH_SCORE, type = Range.Type.FROM, includedBoundary = false)
    private Integer mathScoreFrom;

    @Must
    @Wildcard
    private String name;

    @MustNot
    @Term(fieldName = StuFieldName.ADDRESS)
    private String excludedAddress;

    @Should
    @Match(operator = Match.Operator.AND)
    private String introduce;

    @Filter
    @MatchPhrase(fieldName = StuFieldName.NAME)
    private String nameMatchPhrase;

    /**
     * this field will affect query in customXxQueries method
     */
    private String customField;

}
