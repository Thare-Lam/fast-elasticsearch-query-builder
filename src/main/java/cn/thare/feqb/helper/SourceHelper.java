package cn.thare.feqb.helper;

import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.Collection;

public class SourceHelper {

    @SuppressWarnings("unchecked")
    public static void set(SearchSourceBuilder searchSource, Object value) {
        searchSource.fetchSource(((Collection<String>) value).toArray(new String[0]), new String[0]);
    }

}
