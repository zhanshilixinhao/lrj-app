package com.lrj.util;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 
 * @ClassName:     Messages
 * @Description:   配置文件读取
 * @author:    	   ZhaoYuBo
 * @date:          2015-8-20 10:19:58
 *
 */
public class MessagesUtil {
	private static final String BUNDLE_NAME = "messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private MessagesUtil() {}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return null;
		}
	}
}
