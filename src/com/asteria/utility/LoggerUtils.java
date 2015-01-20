package com.asteria.utility;

import java.util.logging.Logger;

/**
 * The static-utility class that contains array utility functions.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class LoggerUtils {

    /**
     * The default constructor.
     * 
     * @throws InstantiationError
     *             if this class is instantiated.
     */
    private LoggerUtils() {
        throw new InstantiationError("This class cannot be instantiated!");
    }

    /**
     * Creates a new logger instance using {@link Class#getSimpleName()} of
     * {@code clazz}.
     * 
     * @param clazz
     *            the class to create the new logger with.
     * @return the logger instance.
     */
    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz.getSimpleName());
    }
}
