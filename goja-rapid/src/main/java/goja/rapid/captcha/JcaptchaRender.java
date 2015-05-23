package goja.rapid.captcha;

import com.jfinal.render.Render;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * <p> </p>
 *
 * @author sogYF
 * @version 1.0
 * @since JDK 1.6
 */
public class JcaptchaRender extends Render {

    public static ImageCaptchaService service = new DefaultManageableImageCaptchaService();

    @Override
    public void render() {
        // Set to expire far in the past.
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");

        // return a jpeg
        response.setContentType("image/jpeg");


        // create the image with the text
        BufferedImage bi = service.getImageChallengeForID(request.getSession(true).getId());

        ServletOutputStream out = null;

        // write the data out
        try {
            out = response.getOutputStream();
            ImageIO.write(bi, "jpg", out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(out);
        }

    }


    /**
     * Verification image verification code.
     *
     * @param request             the http request.
     * @param userCaptchaResponse captcha ok!
     * @return true has ok!
     */
    public static boolean validateResponse(HttpServletRequest request, String userCaptchaResponse) {
        //if no session found
        if (request.getSession(false) == null) return false;
        //else use service and session id to validate
        boolean validated = false;
        try {
            validated = service.validateResponseForID(request.getSession().getId(), userCaptchaResponse);
        } catch (CaptchaServiceException e) {
            //do nothing.. false
        }
        return validated;
    }
}
