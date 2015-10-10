package uk.philiphendry.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectUtilities {
	
	public static List<Map<String, Object>> mapListOfProperties(List<?> list) throws Exception {
		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		for (Object object : list) {
			newList.add(mapProperties(object));
		}
		return newList;
	}
	
	public static Map<String, Object> mapProperties(Object bean) throws Exception {
	    Map<String, Object> properties = new HashMap<String, Object>();
	    for (Method method : bean.getClass().getDeclaredMethods()) {
	        if (Modifier.isPublic(method.getModifiers())
	            && method.getParameterTypes().length == 0
	            && method.getReturnType() != void.class
	            && method.getName().matches("^(get|is).+")
	        ) {
	            String name = method.getName().replaceAll("^(get|is)", "");
	            name = Character.toLowerCase(name.charAt(0)) + (name.length() > 1 ? name.substring(1) : "");
	            Object value = method.invoke(bean);
	            properties.put(name, value);
	        }
	    }
	    return properties;
	}
	
}
