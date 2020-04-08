package com.byc.permission.shiro.mvc.param;

import com.byc.common.utils.TimeFormatter;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
/**公共查询参数*/
public class QueryParam {
    private String start;
    private String end;
    private String cond;
    private String sort;    //排序方式
    private String field;   //排序字段
    private int limit;
    private int page;

    public Long getStart() {
        if(StringUtils.isEmpty(start))
            return null;
        return TimeFormatter.formatStr(TimeFormatter.Format.SIMPLE,start);
    }

    public Long getEnd() {
        if(StringUtils.isEmpty(end))
            return null;
        return TimeFormatter.formatStr(TimeFormatter.Format.SIMPLE,end);
    }

    public boolean asc(){
        return "".equals(sort);
    }
}
