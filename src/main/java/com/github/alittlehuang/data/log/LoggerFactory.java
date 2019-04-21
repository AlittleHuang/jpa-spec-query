package com.github.alittlehuang.data.log;


import lombok.experimental.Delegate;

/**
 * @author ALittleHuang
 */
public class LoggerFactory {

    private static final Logger DISABLE_LOGGER = new Logger() {
    };

    private static final boolean SLF4J_ENABLED = logEnabled("org.slf4j.LoggerFactory");
    private static final boolean LOG4J2_ENABLED = logEnabled("org.apache.logging.log4j.LogManager");
    private static final boolean LOG4J_ENABLED = logEnabled("org.apache.log4j.Logger");

    private static boolean logEnabled(String className) {
        boolean logEnabled;
        try {
            Class.forName(className);
            logEnabled = true;
        } catch ( ClassNotFoundException e ) {
            logEnabled = false;
        }
        return logEnabled;
    }

    public static Logger getLogger(Class<?> clazz) {
        if ( SLF4J_ENABLED ) {
            return new Slf4j(org.slf4j.LoggerFactory.getLogger(clazz));
        }

        if ( LOG4J2_ENABLED ) {
            return new Log4j2(org.apache.logging.log4j.LogManager.getLogger(clazz));
        }

        if ( LOG4J_ENABLED ) {
            return new Log4j(org.apache.log4j.LogManager.getLogger(clazz));
        }

        return DISABLE_LOGGER;
    }

    private static class Slf4j implements Logger {
        @Delegate
        private final org.slf4j.Logger logger;

        Slf4j(org.slf4j.Logger logger) {
            this.logger = logger;
        }
    }

    private static class Log4j implements Logger {
        @Delegate
        private final org.apache.log4j.Logger logger;

        Log4j(org.apache.log4j.Logger logger) {
            this.logger = logger;
        }
    }

    private static class Log4j2 implements Logger {
        @Delegate
        private final org.apache.logging.log4j.Logger logger;

        Log4j2(org.apache.logging.log4j.Logger logger) {
            this.logger = logger;
        }
    }


}
