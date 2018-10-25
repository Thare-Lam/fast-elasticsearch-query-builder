package cn.thare.feqb.helper;

import cn.thare.feqb.annotation.func.Highlighters;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import java.lang.annotation.Annotation;
import java.util.Collection;

public class HighlightHelper {

    @SuppressWarnings("unchecked")
    public static void set(SearchSourceBuilder searchSource, Annotation annotation, Object value) {
        Highlighters highlighters = (Highlighters) annotation;
        HighlightBuilder highlight = new HighlightBuilder().highlighterType(highlighters.type());
        ((Collection<String>) value).forEach(highlight::field);
        searchSource.highlighter(highlight);
    }

}
