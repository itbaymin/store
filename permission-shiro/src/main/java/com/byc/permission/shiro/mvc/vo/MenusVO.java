package com.byc.permission.shiro.mvc.vo;

import lombok.Data;

/**菜单接口响应数据*/
@Data
public class MenusVO {

    @Data
    public static class MenuData{
        private String name;
        private String sort;

    }
}
