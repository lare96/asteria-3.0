package com.asteria.utility;

import java.util.logging.Logger;

/**
 * The static-utility class that contains logger utility functions.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class LoggerUtils {

    /**
     * The default constructor.
     *
     * @throws UnsupportedOperationException
     *             if this class is instantiated.
     */
    private LoggerUtils() {
        throw new UnsupportedOperationException("This class cannot be " + "instantiated!");
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

    /**
     * Creates a new logger instance using {@code Asteria317} as the name.
     * Please note that this method <b>does not</b> return a static-logger
     * (because it wouldn't be thread safe), meaning that when this is used the
     * logger instance should still be cached. An example below:
     * <p>
     * <p>
     * 
     * <pre>
     * Logger log = Logger.getGlobal(); // Get a new logger, cache it.
     * 
     * // And now we can use it wherever.
     * log.info(&quot;Hello, World!&quot;);
     * log.warning(&quot;Uh oh, a warning!&quot;);
     * </pre>
     *
     * @return the logger instance.
     */
    public static Logger getGlobal() {
        return Logger.getLogger("Asteria317");
    }
}
