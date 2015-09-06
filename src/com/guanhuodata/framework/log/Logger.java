package com.guanhuodata.framework.log;

import com.guanhuodata.framework.log.loggerTool.CTOMSLogger;

public abstract class Logger {

	public Logger() {
	}

	public static <T> Logger getLogger(Class<T> c) {
		return new CTOMSLogger(c.getName());
	}

	public void log(LogLevel level, String msg) {
		throw new UnsupportedOperationException("Write log DOES NOT support on this logger.");
	}

	public void record(Log log) {
		throw new UnsupportedOperationException("Write log DOES NOT support on this logger.");
	}
}
