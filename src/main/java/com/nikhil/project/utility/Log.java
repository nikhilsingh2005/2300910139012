package com.nikhil.project.utility;

import com.nikhil.project.service.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Log {

    private static LoggingService loggingService;

    @Autowired
    public Log(LoggingService loggingService) {
        Log.loggingService = loggingService;
    }

    public static void log(String stack, String level, String packageName, String message) {
        loggingService.log(stack.toLowerCase(), level.toLowerCase(), packageName.toLowerCase(), message);
    }
}
