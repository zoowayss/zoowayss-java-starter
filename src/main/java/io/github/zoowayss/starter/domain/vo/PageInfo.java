package io.github.zoowayss.starter.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo<T> implements Serializable {

    private static final long serialVersionUID = 3284556208871536609L;

    // 总记录数
    private long total;

    // 结果集
    private List<T> records;

    private Long cursor;

    public PageInfo(long total, List<T> records) {
        this.total = total;
        this.records = records;
    }
}
