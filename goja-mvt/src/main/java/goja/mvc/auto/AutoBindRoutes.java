/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */
package goja.mvc.auto;

import com.google.common.base.Preconditions;
import com.jfinal.config.Routes;
import com.jfinal.kit.StrKit;
import goja.Logger;
import goja.StringPool;
import goja.annotation.Path;
import goja.initialize.ctxbox.ClassBox;
import goja.initialize.ctxbox.ClassType;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@SuppressWarnings("unchecked")
public class AutoBindRoutes extends Routes {


    private static final String suffix = "Controller";

    @Override
    public void config() {
        List<Class> controllerClasses = ClassBox.getInstance().getClasses(ClassType.CONTROLLER);
        if (controllerClasses != null && !controllerClasses.isEmpty()) {
            Path path;
            for (Class controller : controllerClasses) {
                path = (Path) controller.getAnnotation(Path.class);
                if (path == null) {
                    final String controllerKey = controllerKey(controller);
                    this.add(controllerKey, controller);
                    Logger.debug("routes.add(" + controllerKey + ", " + controller.getName() + StringPool.RIGHT_BRACKET);
                } else if (StrKit.isBlank(path.viewPath())) {
                    this.add(path.value(), controller);
                    Logger.debug("routes.add(" + path.value() + ", " + controller.getName() + StringPool.RIGHT_BRACKET);
                } else {
                    this.add(path.value(), controller, path.viewPath());
                    Logger.debug("routes.add(" + path.value() + ", " + controller + StringPool.COMMA
                            + path.viewPath() + StringPool.RIGHT_BRACKET);
                }
            }
        }
    }

    private static String controllerKey(Class clazz) {
        final String simpleName = clazz.getSimpleName();
        Preconditions.checkArgument(simpleName.endsWith(suffix),
                " does not has a @Path annotation and it's name is not end with " + suffix);
        String controllerKey = StringPool.SLASH + StrKit.firstCharToLowerCase(simpleName);
        controllerKey = controllerKey.substring(0, controllerKey.indexOf(suffix));

        String pak_name = clazz.getPackage().getName();
        if (StringUtils.endsWith(pak_name, "controllers")) {
            return controllerKey;
        } else {
            String biz_pk = StringUtils.substringAfter(pak_name, ".controllers.");
            biz_pk = biz_pk.replace(StringPool.DOT, StringPool.SLASH);
            controllerKey = StringPool.SLASH + biz_pk + controllerKey;
            return controllerKey;
        }

    }


}
