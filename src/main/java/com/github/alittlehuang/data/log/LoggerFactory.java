package com.github.alittlehuang.data.log;


import lombok.experimental.Delegate;

/**
 * @author ALittleHuang
 */
public class LoggerFactory {

    private static final Logger DISABLE_LOGGER = new Logger() {
    };

    private static final boolean LOG_ENABLED = logEnabled();

    private static boolean logEnabled() {
        boolean logEnabled;
        try {
            Class.forName("org.slf4j.LoggerFactory");
            logEnabled = true;
        } catch ( ClassNotFoundException e ) {
            logEnabled = false;
        }
        return logEnabled;
    }

    public static Logger getLogger(Class<?> clazz) {
        if ( LOG_ENABLED ) {
            return new Log(org.slf4j.LoggerFactory.getLogger(clazz));
        }

        return DISABLE_LOGGER;
    }

    private static class Log implements Logger {
        @Delegate
        private final org.slf4j.Logger logger;

        Log(org.slf4j.Logger logger) {
            this.logger = logger;
        }
    }


}
