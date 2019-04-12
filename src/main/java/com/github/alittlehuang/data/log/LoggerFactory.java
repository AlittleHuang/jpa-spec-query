package com.github.alittlehuang.data.log;


import lombok.experimental.Delegate;

public class LoggerFactory {
    private static boolean logable;

    static {
        boolean logable;
        try {
            Class.forName("org.slf4j.LoggerFactory");
            logable = true;
        } catch ( ClassNotFoundException e ) {
            logable = false;
        }
        LoggerFactory.logable = logable;
    }

    public static Logger getLogger(Class<?> clazz) {
        if ( logable ) {
            return new Log(org.slf4j.LoggerFactory.getLogger(clazz));
        }

        return new Logger() {
        };
    }

    private static class Log implements Logger {
        @Delegate
        private final org.slf4j.Logger logger;

        Log(org.slf4j.Logger logger) {
            this.logger = logger;
        }
    }


}
