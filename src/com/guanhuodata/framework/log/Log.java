package com.guanhuodata.framework.log;

public class Log implements java.io.Serializable {

	private static final long serialVersionUID = 875587783325256087L;

	private long logTime;
	private String content;
	private LogResult result;

	public long getLogTime() {
		return logTime;
	}

	public void setLogTime(long logTime) {
		this.logTime = logTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LogResult getResult() {
		return result;
	}

	public void setResult(LogResult result) {
		this.result = result;
	}
}
