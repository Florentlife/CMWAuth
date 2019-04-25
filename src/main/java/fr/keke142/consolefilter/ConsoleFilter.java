package fr.keke142.consolefilter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;


public class ConsoleFilter implements Filter {
    private Filter.Result checkMessage(String message) {
        if (message.contains("/connectsite")) {
            return Filter.Result.DENY;
        }
        else if(message.contains("/updatesite mdp"))
        	return Filter.Result.DENY;
        return Filter.Result.NEUTRAL;
    }

    @Override
    public Result getOnMismatch() {
        return Filter.Result.NEUTRAL;
    }

    @Override
    public Result getOnMatch() {
        return Filter.Result.NEUTRAL;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object... objects) {
        return checkMessage(message);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object o) {
        return checkMessage(message);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object o, Object o1) {
        return checkMessage(message);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object o, Object o1, Object o2) {
        return checkMessage(message);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object o, Object o1, Object o2, Object o3) {
        return checkMessage(message);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object o, Object o1, Object o2, Object o3, Object o4) {
        return checkMessage(message);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object o, Object o1, Object o2, Object o3, Object o4, Object o5) {
        return checkMessage(message);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
        return checkMessage(message);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
        return checkMessage(message);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
        return checkMessage(message);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9) {
        return checkMessage(message);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object message, Throwable throwable) {
        return checkMessage(message.toString());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message message, Throwable throwable) {
        return checkMessage(message.getFormattedMessage());
    }

    @Override
    public Result filter(LogEvent logEvent) {
        return checkMessage(logEvent.getMessage().getFormattedMessage());
    }

    @Override
    public State getState() {
        try {
            return LifeCycle.State.STARTED;
        } catch (Exception localException) {
            return null;
        }
    }

    @Override
    public void initialize() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isStarted() {
        return false;
    }

    @Override
    public boolean isStopped() {
        return false;
    }
}
