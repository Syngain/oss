package com.guanhuodata.framework.util;


public class IDGenerator {
    public static String generateId(){
        return generateId("-");
    }
    public static String generateId(String sep){
        if(sep == null){
            sep = "-";
        }
        UUID uuid = new UUID(sep);
        return uuid.toString();
    }
    
    public static void main(String[] args) {
		generateId();
	}
}
