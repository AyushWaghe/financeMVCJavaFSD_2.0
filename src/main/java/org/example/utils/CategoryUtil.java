package org.example.utils;

public class CategoryUtil {

    public static String normalizeString(String str){
        if(str==null) return null;
        return str.trim().toLowerCase();
    }

    public static String categoryDisplay(String str){
        if (str==null) return null;

        String modifiedString=str.trim().toLowerCase();

        return Character.toUpperCase(modifiedString.charAt(0))+str.substring(1);
    }
}
