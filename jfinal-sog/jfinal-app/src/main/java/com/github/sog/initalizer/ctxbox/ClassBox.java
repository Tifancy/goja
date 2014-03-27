/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.github.sog.initalizer.ctxbox;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.Controller;
import com.github.sog.initalizer.AppLoadEvent;
import com.jfinal.plugin.activerecord.Model;
import org.quartz.Job;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-01-19 22:18
 * @since JDK 1.6
 */
public class ClassBox {
    /**
     * 获取单例对象,如果要调用该单例的使用,只能通过该方法获取.
     */
    public static ClassBox getInstance() {
        return ClassBoxHolder.instance;
    }

    private static final Map<ClassType, List<Class>> CLASS_BOX_MAP = Maps.newHashMap();

    public List<Class> getClasses(ClassType classType) {
        return CLASS_BOX_MAP.get(classType);
    }

    void push(Class<?> cls) {
        // Check the class categories.
        if (Model.class.isAssignableFrom(cls)) {
            initClassWithType(cls, ClassType.MODEL);
        } else if (Controller.class.isAssignableFrom(cls)) {
            initClassWithType(cls, ClassType.CONTROLLER);
        } else if (Job.class.isAssignableFrom(cls)) {
            initClassWithType(cls, ClassType.JOB);
        } else if (AppLoadEvent.class.isAssignableFrom(cls)) {
            initClassWithType(cls, ClassType.APP);
        } else if (Interceptor.class.isAssignableFrom(cls)) {
            initClassWithType(cls, ClassType.AOP);
        } else {
            //ignore
        }
    }

    public void initClassWithType(Class<?> cls, final ClassType type) {
        List<Class> classes = CLASS_BOX_MAP.get(type);
        if (classes == null) {
            classes = Lists.newArrayList();
        } else {
            if (classes.contains(cls)) {
                return;
            }
        }
        classes.add(cls);
        CLASS_BOX_MAP.put(type, classes);
    }

    /**
     * 私有构造函数,确保对象只能通过单例方法来调用.
     */
    private ClassBox() {
    }

    public void clearBox() {
        CLASS_BOX_MAP.clear();

    }

    /**
     * lazy 加载的内部类,用于实例化单例对象.
     */
    private static class ClassBoxHolder {
        static ClassBox instance = new ClassBox();
    }
}
