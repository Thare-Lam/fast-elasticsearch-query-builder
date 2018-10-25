package cn.thare.feqb.helper;

import cn.thare.feqb.able.Sortable;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;

public class SortHelper {

    @SuppressWarnings("unchecked")
    public static void set(SearchSourceBuilder searchSource, Object value) {
        ((List<Sortable>) value).forEach(sortable ->
                searchSource.sort(sortable.fieldName(), SortOrder.fromString(sortable.order().value())));
    }

}
