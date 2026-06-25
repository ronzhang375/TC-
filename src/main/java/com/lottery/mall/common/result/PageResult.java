package com.lottery.mall.common.result;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

/**
 * 分页结果
 */
@Data
@NoArgsConstructor
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 总记录数 */
    private Long total;

    /** 当前页数据 */
    private List<T> rows;

    /** 当前页码 */
    private Integer pageNum;

    /** 每页记录数 */
    private Integer pageSize;

    /** 总页数 */
    private Integer totalPages;

    /** 是否有上一页 */
    private Boolean hasPrevious;

    /** 是否有下一页 */
    private Boolean hasNext;

    public PageResult(List<T> rows, Long total, Integer pageNum, Integer pageSize) {
        this.rows = rows;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) total / pageSize);
        this.hasPrevious = pageNum > 1;
        this.hasNext = pageNum < totalPages;
    }

    /**
     * 构建分页结果
     */
    public static <T> PageResult<T> of(List<T> rows, Long total, Integer pageNum, Integer pageSize) {
        return new PageResult<>(rows, total, pageNum, pageSize);
    }
}
