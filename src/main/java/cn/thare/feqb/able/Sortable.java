package cn.thare.feqb.able;

import cn.thare.feqb.enums.SortOrder;

public interface Sortable {

    String fieldName();

    SortOrder order();

}
