package cn.thare.feqb.test;

import cn.thare.feqb.annotation.func.*;
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

    @TermsAggregation(name = "ageAggregation", field = StuFieldName.AGE, maxSize = 5, order = TermsAggregation.Order.KEY_ASC)
    private Integer ageAggregation;

    @Sort
    private List<StuSortEnum> sorts;

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

}
