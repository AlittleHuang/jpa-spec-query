package com.github.alittlehuang.data.log;

import org.slf4j.Marker;


public interface Logger {

    String ROOT_LOGGER_NAME = "ROOT";

    default String getName() {
        return "";
    }

    default boolean isTraceEnabled() {
        return false;
    }

    default void trace(String msg) {
    }

    default void trace(String format, Object arg) {
    }

    default void trace(String format, Object arg1, Object arg2) {
    }

    default void trace(String format, Object... arguments) {
    }

    default void trace(String msg, Throwable t) {
    }

    default boolean isTraceEnabled(Marker marker) {
        return false;
    }

    default void trace(Marker marker, String msg) {
    }

    default void trace(Marker marker, String format, Object arg) {
    }


    default void trace(Marker marker, String format, Object arg1, Object arg2) {
    }


    default void trace(Marker marker, String format, Object... argArray) {
    }

    default void trace(Marker marker, String msg, Throwable t) {
    }

    default boolean isDebugEnabled() {
        return false;
    }

    default void debug(String msg) {
    }

    default void debug(String format, Object arg) {
    }

    default void debug(String format, Object arg1, Object arg2) {
    }

    default void debug(String format, Object... arguments) {
    }

    default void debug(String msg, Throwable t) {
    }

    default boolean isDebugEnabled(Marker marker) {
        return false;
    }

    default void debug(Marker marker, String msg) {
    }

    
    default void debug(Marker marker, String format, Object arg) {
    }

    
    default void debug(Marker marker, String format, Object arg1, Object arg2) {
    }

    
    default void debug(Marker marker, String format, Object... arguments) {
    }

    
    default void debug(Marker marker, String msg, Throwable t) {
    }

    
    default boolean isInfoEnabled() {
        return false;
    }

    
    default void info(String msg) {
    }

    
    default void info(String format, Object arg) {
    }

    
    default void info(String format, Object arg1, Object arg2) {
    }

    
    default void info(String format, Object... arguments) {
    }

    
    default void info(String msg, Throwable t) {
    }

    
    default boolean isInfoEnabled(Marker marker) {
        return false;
    }

    
    default void info(Marker marker, String msg) {
    }

    
    default void info(Marker marker, String format, Object arg) {
    }

    
    default void info(Marker marker, String format, Object arg1, Object arg2) {
    }

    
    default void info(Marker marker, String format, Object... arguments) {
    }

    
    default void info(Marker marker, String msg, Throwable t) {
    }

    
    default boolean isWarnEnabled() {
        return false;
    }

    
    default void warn(String msg) {
    }

    
    default void warn(String format, Object arg) {
    }

    
    default void warn(String format, Object... arguments) {
    }

    
    default void warn(String format, Object arg1, Object arg2) {
    }

    
    default void warn(String msg, Throwable t) {
    }

    
    default boolean isWarnEnabled(Marker marker) {
        return false;
    }

    
    default void warn(Marker marker, String msg) {
    }

    
    default void warn(Marker marker, String format, Object arg) {
    }

    
    default void warn(Marker marker, String format, Object arg1, Object arg2) {
    }

    
    default void warn(Marker marker, String format, Object... arguments) {
    }

    
    default void warn(Marker marker, String msg, Throwable t) {
    }

    
    default boolean isErrorEnabled() {
        return false;
    }

    
    default void error(String msg) {
    }

    
    default void error(String format, Object arg) {
    }

    
    default void error(String format, Object arg1, Object arg2) {
    }

    
    default void error(String format, Object... arguments) {
    }

    
    default void error(String msg, Throwable t) {
    }

    
    default boolean isErrorEnabled(Marker marker) {
        return false;
    }

    
    default void error(Marker marker, String msg) {
    }

    
    default void error(Marker marker, String format, Object arg) {
    }

    
    default void error(Marker marker, String format, Object arg1, Object arg2) {
    }

    
    default void error(Marker marker, String format, Object... arguments) {
    }

    
    default void error(Marker marker, String msg, Throwable t) {
    }
}
