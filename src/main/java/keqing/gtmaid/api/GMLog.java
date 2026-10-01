package keqing.gtmaid.api;

import org.slf4j.Logger;

public class GMLog {
    public static Logger logger;

    public GMLog() {
    }

    public static void init(Logger modLogger) {
        logger = modLogger;
    }

    public static void info(String msg, Object... args) {
        if (logger != null) {
            logger.info(msg, args);
        }
    }

    public static void warn(String msg, Object... args) {
        if (logger != null) {
            logger.warn(msg, args);
        }
    }

    public static void error(String msg, Object... args) {
        if (logger != null) {
            logger.error(msg, args);
        }
    }
}
