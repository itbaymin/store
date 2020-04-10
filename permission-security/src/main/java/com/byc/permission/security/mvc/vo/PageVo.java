package com.byc.permission.security.mvc.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by baiyc
 * 2020/4/10/010 09:55
 * Description：分页包装响应
 */
@Data
public class PageVo<T> implements Serializable {
    private int page;
    private int size;
    private long total;
    private List<T> list;

    public int getPage(){
        return page+1;
    }
}