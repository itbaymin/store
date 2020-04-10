package com.byc.permission.security.mvc.vo;

import com.byc.persisent.permission.entity.SysMenu;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**菜单接口响应数据*/
@Data
public class MenusVO {
    private List<MenuData> menus;

    @Data
    public static class MenuData{
        private String name;
        private int sort;
        private String permission;
        private String uri;
        private String icon;
        private List<MenuData> children;
    }

    public static MenuData build(SysMenu menu){
        MenuData data = new MenuData();
        data.setName(menu.getMenuname());
        data.setSort(menu.getSort());
        data.setPermission(menu.getPermission());
        data.setUri(menu.getUri());
        data.setIcon(menu.getIcon());
        List<SysMenu> children = menu.getChildren();
        if(!CollectionUtils.isEmpty(children)){
            List<MenuData> collect = children.stream().map(MenusVO::build).sorted(Comparator.comparing(MenuData::getSort)).collect(Collectors.toList());
            data.setChildren(collect);
        }
        return data;
    }

    public static MenusVO build(List<SysMenu> menus){
        List<MenuData> collect = menus.stream().filter(menu -> menu.isDisplay() && menu.isEnable()).map(MenusVO::build).sorted(Comparator.comparing(MenusVO.MenuData::getSort)).collect(Collectors.toList());
        MenusVO vo = new MenusVO();
        vo.setMenus(collect);
        return vo;
    }
}
