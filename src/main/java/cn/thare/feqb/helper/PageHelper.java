package cn.thare.feqb.helper;

import cn.thare.feqb.annotation.func.PageNo;
import cn.thare.feqb.annotation.func.PageSize;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Optional;

@Slf4j
public class PageHelper {

    private Integer pageNo;

    private Integer pageSize;

    public static PageHelper create() {
        return new PageHelper();
    }

    public void fill(Annotation annotation, Field field, Object value) {
        if (value instanceof Integer) {
            if (PageNo.class == annotation.annotationType()) {
                pageNo = (Integer) value;
            } else if (PageSize.class == annotation.annotationType()) {
                pageSize = (Integer) value;
            }
        } else {
            log.warn("{}'s field type is not Integer", field.getName());
        }
    }

    public void set(SearchSourceBuilder searchSource, PageTool pageTool) {
        Optional.ofNullable(pageTool).ifPresent(pt ->
                Optional.ofNullable(pt.getPageInfoSafe(pageNo, pageSize)).ifPresent(pi ->
                        searchSource.from(pi.getFrom()).size(pi.getSize())));
    }

}
