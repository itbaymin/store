package com.byc.permission.shiro.mvc.param;

import com.byc.common.utils.TimeFormatter;
import lombok.Data;

@Data
/**公共查询参数*/
public class QueryParam {
    private String start;
    private String end;
    private String cond;
    private int limit;
    private int page;

    public Long getStart() {
        return TimeFormatter.formatStr(TimeFormatter.Format.SIMPLE,start);
    }

    public Long getEnd() {
        return TimeFormatter.formatStr(TimeFormatter.Format.SIMPLE,end);
    }
}
