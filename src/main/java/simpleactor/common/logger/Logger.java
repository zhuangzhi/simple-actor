package simpleactor.common.logger;

import org.slf4j.LoggerFactory;

/**
 * This is used for my debug. Just use the native  org.slf4j.Logger in production.
 */
public final class Logger {

    //private static final org.slf4j.Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    private org.slf4j.Logger log = null;

    private static boolean printOut = false ;

    public static void enableOut() {
        printOut = true ;
    }
    public static void enableOut(boolean enable) {
        printOut = enable ;
    }


    public static Logger getLogger(Class<?> type) {
        return new Logger(type);
    }

    public org.slf4j.Logger log() {
        return this.log ;
    }
    private Logger(Class<?> type) {
        this.log = LoggerFactory.getLogger(type);
    }


    public void info(String msg) {
        log.info(msg);
        if (printOut) {
            System.out.println(msg);
        }
    }
    public void info(String msg, Throwable e) {
        log.info(msg, e);
        if (printOut) {
            System.out.print(msg);
            e.printStackTrace();
        }
    }

    public void info(String msg, Object o) {
        log.info(msg, o);
        if (printOut) {
            System.out.println(msg + "-" + o.toString());
        }

    }
    public void debug(String msg) {
        log.debug(msg);
        if (printOut) {
            System.out.println(msg);
        }
    }
    public void debug(String msg, Object o) {
        log.debug(msg, o);
        if (printOut) {
            System.out.println(msg + "-" + o.toString());
        }

    }

    public void debug(String msg, Object ... objects) {
        log.debug(msg, objects);
        if (printOut) {
            System.out.print(msg + "-");
            for (Object object : objects) {
                System.out.print(object+",");
            }
        }
    }

    public void info(String msg, Object ... objects) {
        log.info(msg, objects);
        if (printOut) {
            System.out.print(msg + "-");
            for (Object object : objects) {
                System.out.print(object+",");
            }
        }
    }

    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    public void warn(String msg) {
        log.warn(msg);
        if (printOut) {
            System.out.println(msg);
        }
    }
}
