package util;

import java.util.logging.Logger;

/**
 * Created by mac on 2017/3/27.
 */

/****************************************
 * [INFO]========== ==========
 * [warning]========== ==========
 *
 *
 *
 *****************************************/


public class LogRecord {
    static String strClassName = LogRecord.class.getName();
    public static Logger logger = Logger.getLogger(strClassName);
    private static LogRecord ourInstance = new LogRecord();

    private LogRecord() {
    }

    public static LogRecord getInstance() {
        return ourInstance;
    }
}
