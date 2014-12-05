package goja.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

/**
 * <p> </p>
 *
 * @author sogYF
 * @version 1.0
 * @since JDK 1.6
 */
public abstract class LoginKit {

    public static void login(String username, String password, boolean rememberMe) throws AuthenticationException {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        final Subject subject = SecurityUtils.getSubject();
        subject.login(token);
    }

    public static void login(String username, String password) throws AuthenticationException {
        login(username, password, false);
    }


}
