package com.byc.permission.security.mvc.param;

import com.byc.common.utils.TimeFormatter;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
/**公共查询参数*/
public class QueryParam {
    private String start;
    private String end;
    private String cond = "";
    private String sort = "desc";    //排序方式
    private String field = "id";   //排序字段
    private int limit = 10;
    private int page = 1;

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
        return "asc".equals(sort.toLowerCase());
    }

    public int getPage(){
        return page-1;
    }
}
