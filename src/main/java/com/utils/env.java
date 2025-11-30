package com.utils;
import io.github.cdimascio.dotenv.Dotenv;


public class env {
    
    public static Dotenv dotenv;
    public static void loadEnv() {
        dotenv = Dotenv.load();
    }
    public static String getEnv(String key) {
        if (dotenv == null) {
            loadEnv();
        }
        return dotenv.get(key);
    }
    
}
