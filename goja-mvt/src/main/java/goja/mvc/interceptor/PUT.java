package goja.mvc.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

/**
 * <p> </p>
 *
 * @author sogYF
 * @version 1.0
 * @since JDK 1.6
 */
public class PUT implements Interceptor {
    public void intercept(ActionInvocation ai) {
        Controller controller = ai.getController();
        if ("PUT".equalsIgnoreCase(controller.getRequest().getMethod().toUpperCase()))
            ai.invoke();
        else
            controller.renderError(404);
    }
}
