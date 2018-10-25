package cn.thare.feqb.test;

import cn.thare.feqb.able.Sortable;
import cn.thare.feqb.enums.SortOrder;

public enum StuSortEnum implements Sortable {

    SCORE_ASC("_score", SortOrder.ASC),
    SCORE_DESC("_score", SortOrder.DESC),
    MATH_SCORE_ASC(StuFieldName.MATH_SCORE, SortOrder.ASC),
    MATH_SCORE_DESC(StuFieldName.MATH_SCORE, SortOrder.ASC);

    private String fieldName;

    private SortOrder sortOrder;

    StuSortEnum(String fieldName, SortOrder sortOrder) {
        this.fieldName = fieldName;
        this.sortOrder = sortOrder;
    }

    @Override
    public String fieldName() {
        return this.fieldName;
    }

    @Override
    public SortOrder order() {
        return this.sortOrder;
    }
}
