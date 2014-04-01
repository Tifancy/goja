package com.jfinal.module.wxchat.api.beans;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * <p>
 * .
 * </p>
 *
 * @author Jerry Ou
 * @version 1.0 2014-03-27 22:03
 * @since JDK 1.6
 */
public class MenuButton {

    public List<Menu> button = Lists.newArrayList();

    public List<Menu> getButton() {
        return button;
    }

    public void setButton(List<Menu> button) {
        this.button.addAll(button);
    }

    /**
     * 添加一个菜单
     * @param menu
     */
    public void addMenu(Menu menu) {
        this.button.add(menu);
    }

    /**
     * 转成JSON对象
     *
     * @return
     */
    public String toJSONString() {
        return JSON.toJSONString(this);
    }
}
