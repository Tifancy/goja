package app.controllers;

import com.jfinal.config.BasicController;
import com.jfinal.core.ActionKey;

/**
 * <p>
 * Default index Controller.
 * </p>
 */
public class IndexController extends BasicController {

    @ActionKey("/")
    public void index(){
    	setAttr("app_name", "{{appName}}");
        render("/index.ftl");
    }
}