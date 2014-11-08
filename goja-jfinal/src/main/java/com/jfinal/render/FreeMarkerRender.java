/**
 * Copyright (c) 2011-2015, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jfinal.render;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletContext;

import com.google.common.base.Charsets;
import com.google.common.cache.CacheLoader;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

/**
 * FreeMarkerRender.
 */
public class FreeMarkerRender extends Render {

	private static final String        encoding    = getEncoding();
	private static final String        contentType = "text/html; charset=" + encoding;

	private static final Version FREEMARKER_VERSION = Configuration.getVersion();
	private static final Configuration config      = new Configuration(FREEMARKER_VERSION);


	public FreeMarkerRender(String view) {
		this.view = view;
	}

	/**
	 * freemarker can not load freemarker.properies automatically
	 */
	public static Configuration getConfiguration() {
		return config;
	}

	/**
	 * Set freemarker's property.
	 * The value of template_update_delay is 5 seconds.
	 * Example: FreeMarkerRender.setProperty("template_update_delay", "1600");
	 */
	public static void setProperty(String propertyName, String propertyValue) {
		try {
			FreeMarkerRender.getConfiguration().setSetting(propertyName, propertyValue);
		} catch (TemplateException e) {
			throw new RuntimeException(e);
		}
	}

	public static void setProperties(Properties properties) {
		try {
			FreeMarkerRender.getConfiguration().setSettings(properties);
		} catch (TemplateException e) {
			throw new RuntimeException(e);
		}
	}

	static void init(ServletContext servletContext, Locale locale, int template_update_delay) {
		// Initialize the FreeMarker configuration;
		// - Create a configuration instance
		// config = new Configuration();
		// - Templates are stoted in the WEB-INF/templates directory of the Web app.
		config.setServletContextForTemplateLoading(servletContext, "/");	// "WEB-INF/templates"
        // - Set update dealy to 0 for now, to ease debugging and testing.
        //   Higher value should be used in production environment.
        if (getDevMode()) {
        	config.setTemplateUpdateDelay(0);
       	}
        else {
        	config.setTemplateUpdateDelay(template_update_delay);
        }
        
        // - Set an error handler that prints errors so they are readable with
        //   a HTML browser.
        // config.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        
        // - Use beans wrapper (recommmended for most applications)
        config.setObjectWrapper(new BeansWrapperBuilder(FREEMARKER_VERSION).build());
        // - Set the default charset of the template files
        config.setDefaultEncoding(encoding);		// config.setDefaultEncoding("ISO-8859-1");
        // - Set the charset of the output. This is actually just a hint, that
        //   templates may require for URL encoding and for generating META element
        //   that uses http-equiv="Content-type".
        config.setOutputEncoding(encoding);			// config.setOutputEncoding("UTF-8");
        // - Set the default locale
        config.setLocale(locale /* Locale.CHINA */ );		// config.setLocale(Locale.US);
        config.setLocalizedLookup(false);
        
        // 去掉int型输出时的逗号, 例如: 123,456
        // config.setNumberFormat("#");		// config.setNumberFormat("0"); 也可以
        config.setNumberFormat("#0.#####");
        config.setDateFormat("yyyy-MM-dd");
        config.setTimeFormat("HH:mm:ss");
        config.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
    }
    
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void render() {
		response.setContentType(contentType);
        
		Map root = new HashMap();
		for (Enumeration<String> attrs=request.getAttributeNames(); attrs.hasMoreElements();) {
			String attrName = attrs.nextElement();
			root.put(attrName, request.getAttribute(attrName));
		}
		
		PrintWriter writer = null;
        try {
			Template template = config.getTemplate(view);
			writer = response.getWriter();
			template.process(root, writer);		// Merge the data-model and the template
		} catch (Exception e) {
			throw new RenderException(e);
		}
		finally {
			if (writer != null)
				writer.close();
		}
	}
}

