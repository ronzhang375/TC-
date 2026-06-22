package com.lottery.mall.common.core;

import lombok.Data;
import java.io.Serializable;

/**
 * 分页请求参数
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 页码 */
    private Integer pageNum = 1;

    /** 每页记录数 */
    private Integer pageSize = 10;

    /** 最大每页记录数 */
    private static final Integer MAX_PAGE_SIZE = 100;

    public void setPageNum(Integer pageNum) {
        if (pageNum == null || pageNum < 1) {
            this.pageNum = 1;
        } else {
            this.pageNum = pageNum;
        }
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize == null || pageSize < 1) {
            this.pageSize = 10;
        } else if (pageSize > MAX_PAGE_SIZE) {
            this.pageSize = MAX_PAGE_SIZE;
        } else {
            this.pageSize = pageSize;
        }
    }

    /**
     * 获取偏移量
     */
    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }
}
