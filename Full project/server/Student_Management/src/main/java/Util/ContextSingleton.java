package Util;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ContextSingleton {
    private static volatile InitialContext ctx;

    // Private constructor to prevent instantiation
    private ContextSingleton() {}

    public static InitialContext getContext() {
        if (ctx == null) {
            synchronized (ContextSingleton.class) {
                if (ctx == null) {
                    try {
                        ctx = new InitialContext();
                    } catch (NamingException e) {
                        e.printStackTrace(); // Or use a logger
                    }
                }
            }
        }
        return ctx;
    }
}

