package cn.thare.feqb.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Objects;

/**
 * Set from and size safely
 */
@Builder
public class PageTool {

    @NonNull
    @Builder.Default
    private Integer minPageNo = 1;

    @NonNull
    @Builder.Default
    private Integer maxPageNo = 10000;

    @NonNull
    @Builder.Default
    private Integer defaultPageNo = 1;

    @NonNull
    @Builder.Default
    private Integer minPageSize = 0;

    @NonNull
    @Builder.Default
    private Integer maxPageSize = 100;

    @NonNull
    @Builder.Default
    private Integer defaultPageSize = 20;

    @NonNull
    @Builder.Default
    private Integer maxResultWindow = 10000;

    public static PageTool defaultPageTool() {
        return PageTool.builder().build();
    }

    @AllArgsConstructor
    class PageInfo {

        @Getter
        private Integer from;

        @Getter
        private Integer size;

    }

    public PageInfo getPageInfoSafe(Integer pageNo, Integer pageSize) {
        pageNo = choseValue(pageNo, defaultPageNo, minPageNo, maxPageNo);
        pageSize = choseValue(pageSize, defaultPageSize, minPageSize, maxPageSize);

        Integer from = (pageNo - 1) * pageSize;
        Integer size = pageSize;

        from = from > maxResultWindow ? maxResultWindow : from;
        size = from + size > maxResultWindow ? maxResultWindow - from : size;

        return new PageInfo(from, size);
    }

    private Integer choseValue(Integer target, Integer def, Integer min, Integer max) {
        target = Objects.nonNull(target) ? target : def;
        target = target < min ? min : target;
        target = target > max ? max : target;
        return target;
    }

}
