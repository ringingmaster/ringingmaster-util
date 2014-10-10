package com.concurrentperformance.util.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Thread.UncaughtExceptionHandler;

//TODO move do a different module.

/**
 * Last resort exception handler.
 * 
 * @author Stephen Lake
 */
public class ThreadUncaughtExceptionHelper {

	private static final Logger log = LoggerFactory.getLogger(ThreadUncaughtExceptionHelper.class);
	
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
