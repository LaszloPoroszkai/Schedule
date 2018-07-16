package com.codecool.web.util;

import org.apache.log4j.Logger;


public class Logging<T> {
    private final Logger logging;


    public Logging(Class<T> classType) {
        this.logging = Logger.getLogger(classType);
        logging.debug("Log4j appender configuration is successful by the " + classType + " !!");
    }

    public Logger instance(){
        return logging;
    }


}
