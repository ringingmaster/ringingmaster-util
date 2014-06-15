package com.concurrentperformance.ringingmaster.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.Thread.UncaughtExceptionHandler;

//TODO move do a different module.

/**
 * Last resort exception handler.
 * 
 * @author Stephen Lake
 */
public class ThreadUncaughtExceptionHelper {

	private static final Log log = LogFactory.getLog(LoggingUncaughtExceptionHandler.class);
	
	public static void setLoggingDefaultUncaughtException() {
		Thread.setDefaultUncaughtExceptionHandler(new LoggingUncaughtExceptionHandler());
	}
	
	private static class LoggingUncaughtExceptionHandler implements UncaughtExceptionHandler {

		@Override
		public void uncaughtException(Thread thread, Throwable e) {
			try {
				log.error("UNCAUGHT EXCEPTION in thread [" + thread + "]", e);
			} catch (Throwable t) {
				// Swallow!! If any exception escapes here could get infinite loop. 
			}			
		}		
	}	
}
