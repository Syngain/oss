package com.guanhuodata.framework.log.loggerTool;

import java.util.List;
import java.util.Properties;

public class CTOMSLoggerConfig implements java.io.Serializable{
	
	private static final long serialVersionUID = -6281829312663211062L;
	
	private ConsoleLogger console;
	private FileLogger file;
	private DBLogger db;
	private RootLogger root;

	public ConsoleLogger getConsole() {
		return console;
	}
	public void setConsole(ConsoleLogger console) {
		this.console = console;
	}
	public FileLogger getFile() {
		return file;
	}
	public void setFile(FileLogger file) {
		this.file = file;
	}
	public DBLogger getDb() {
		return db;
	}
	public void setDb(DBLogger db) {
		this.db = db;
	}
	public RootLogger getRoot() {
		return root;
	}
	public void setRoot(RootLogger root) {
		this.root = root;
	}

	public class ConsoleLogger{
		private String id;
		private String level;
		private String formatPattern;
		public String getLevel() {
			return level;
		}
		public void setLevel(String level) {
			this.level = level;
		}
		public String getFormatPattern() {
			return formatPattern;
		}
		public void setFormatPattern(String formatPattern) {
			this.formatPattern = formatPattern;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
	}
	public class FileLogger{
		private String id;
		private String level;
		private String formatPattern;
		private String filePath;
		private String suffix;
		private boolean append;
		public String getLevel() {
			return level;
		}
		public void setLevel(String level) {
			this.level = level;
		}
		public String getFormatPattern() {
			return formatPattern;
		}
		public void setFormatPattern(String formatPattern) {
			this.formatPattern = formatPattern;
		}
		public String getFilePath() {
			return filePath;
		}
		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
		public String getSuffix() {
			return suffix;
		}
		public void setSuffix(String suffix) {
			this.suffix = suffix;
		}
		public boolean isAppend() {
			return append;
		}
		public void setAppend(boolean append) {
			this.append = append;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
	}
	public class DBLogger{
		private String id;
		private Properties dbProperties;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Properties getDbProperties() {
			return dbProperties;
		}

		public void setDbProperties(Properties dbProperties) {
			this.dbProperties = dbProperties;
		}
	}
	
	public class RootLogger{
		private String level;
		private List<String> handlers;
		public String getLevel() {
			return level;
		}
		public void setLevel(String level) {
			this.level = level;
		}
		public List<String> getHandlers() {
			return handlers;
		}
		public void setHandlers(List<String> handlers) {
			this.handlers = handlers;
		}
	}
}
