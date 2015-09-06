/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guanhuodata.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author yaoh
 */
public class JsonUtil {
    public static String makeJsonData(List<Object[]> objects){
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for(Object[] objArr:objects){
            sb.append("[");
            for(Object value:objArr){
                sb.append("\"").append(value==null?"":value.toString()).append("\"").append(",");
            }
            dropLastChar(sb);
            sb.append("]");
            sb.append(",");
        }
        dropLastChar(sb);
        sb.append("]");
        return sb.toString();
    }
    private static void dropLastChar(StringBuffer sb){
        if(sb.charAt(sb.length()-1)==','){
                sb.deleteCharAt(sb.length()-1);
            }
    }
    public static String simpleTranslateEntityToJSON(String[] keys,Object[] values){
        if(keys.length != values.length) return "{}";
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        for(int i=0;i<keys.length;i++){
            sb.append("\"").append(keys[i]).append("\"").append(":");
            if(values[i] != null){
                if(values[i] instanceof String){
                    sb.append("\"").append(values[i].toString()).append("\",");
                }else{
                    sb.append(values[i].toString()).append(",");
                }
            }else{
                sb.append("null").append(",");
            }
        }
        dropLastChar(sb);
        sb.append("}");
        return sb.toString();
    }
    public static String makeJson(Object target) {
        if(target == null){
            return "{}";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        JsonType jsonType;
        Object val = null;
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
        	//System.out.println(field);
            if (field.isAnnotationPresent(JsonTypeSpec.class)) {
                String fieldName = field.getName();
                String stringLetter = fieldName.substring(0, 1).toUpperCase();
                String getName = "get" + stringLetter + fieldName.substring(1);
                try {
                    Method getMethod = target.getClass().getMethod(getName,new Class[] {});
                    val = getMethod.invoke(target, new Object[] {});
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                JsonTypeSpec type = field.getAnnotation(JsonTypeSpec.class);
                jsonType = type.value();
                switch (jsonType) {
                case ARRAY:
                    sb.append("\"").append(parseEscape(fieldName))
                            .append("\":");
                    sb.append("[");
                    if (val != null) {
                        for (Object o : (Object[]) val) {
                            if (o instanceof String) {
                                sb.append("\"")
                                        .append(parseEscape(o.toString()))
                                        .append("\"").append(",");
                            } else if (o instanceof Number) {
                                sb.append(o).append(",");
                            }
                        }
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    sb.append("]").append(",");
                    break;
                case OBJECT:
                    sb.append("\"").append(parseEscape(fieldName))
                            .append("\":");
                    if (val != null) {
                        sb.append(makeJson(val));
                    } else {
                        sb.append("{").append("}");
                    }
                    sb.append(",");
                    break;
                case LIST:
                    sb.append("\"").append(parseEscape(fieldName))
                            .append("\":");
                    sb.append("[");
                    if (val != null) {
                        List listval = (List) val;
                        for (Object o : listval) {
                            if(o instanceof String){
                                sb.append("\"").append(parseEscape(o.toString())).append("\"");
                            }else{
                                sb.append(makeJson(o));
                            }
                            sb.append(",");
                        }
                        if (sb.charAt(sb.length() - 1) == ',') {
                            sb.deleteCharAt(sb.length() - 1);
                        }
                    }
                    sb.append("]").append(",");
                    break;
                case STRING:
                    val = val == null ? "" : val;
                    sb.append("\"").append(parseEscape(fieldName))
                            .append("\":").append("\"")
                            .append(parseEscape(val.toString())).append("\"")
                            .append(",");
                    break;
                case NUMBER:
                    val = val == null ? 0 : val;
                    sb.append("\"").append(parseEscape(fieldName))
                            .append("\":").append(val).append(",");
                    break;
                case BOOLEAN:
                    val = val == null ? Boolean.FALSE : val;
                    sb.append("\"").append(parseEscape(fieldName))
                            .append("\":")
                            .append((Boolean) val ? "true" : "false")
                            .append(",");
                    break;
                }
            }
        }

        sb.append("}");
        int idx = sb.lastIndexOf("}");
        if (idx != -1) {
            char c = sb.charAt(idx - 1);
            if (c == ',') {
                sb.deleteCharAt(idx - 1);
            }
        }
        return sb.toString();
    }

    private static String parseEscape(String field) {
        StringBuffer result = new StringBuffer();
        StringBuffer sb = new StringBuffer(field);
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '\n') {
                result.append("\\n");
            } else if (sb.charAt(i) == '\b') {
                result.append("\\b");
            } else if (sb.charAt(i) == '\f') {
                result.append("\\f");
            } else if (sb.charAt(i) == '\r') {
                result.append("\\r");
            } else if (sb.charAt(i) == '\t') {
                result.append("\\t");
            } else if (sb.charAt(i) == '/') {
                result.append("\\/");
            } else if (sb.charAt(i) == '\"') {
                result.append("\\\"");
            } else if (sb.charAt(i) == '\\') {
                result.append("\\\\");
            } else {
                result.append(sb.charAt(i));
            }
        }
        return result.toString();
    }

    public static <T extends Object> String makeListJson(List<T> objList) {
        StringBuffer sb = new StringBuffer();
        sb.append("{\"Rows\":[");
        for (Object obj : objList) {
            sb.append(makeJson(obj));
            //System.out.println("================================="+makeJson(obj));
            sb.append(",");
        }
        char c = sb.charAt(sb.length() - 1);
        if (c == ',') {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]}");
        return sb.toString();
    }
    public static <T extends Object> String makeListJsonCounts(List<T> objList,int total) {
        StringBuffer sb = new StringBuffer();
        sb.append("{\"Rows\":[");
        for (Object obj : objList) {
            sb.append(makeJson(obj));
            sb.append(",");
        }
        char c = sb.charAt(sb.length() - 1);
        if (c == ',') {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("],\"Total\":"+total+"}");
        return sb.toString();
    }
}
