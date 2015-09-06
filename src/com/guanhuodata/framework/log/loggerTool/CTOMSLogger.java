package com.guanhuodata.framework.log.loggerTool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.guanhuodata.framework.log.LogLevel;
import com.guanhuodata.framework.log.Logger;

public class CTOMSLogger extends Logger {
    private static CTOMSLoggerConfig logconf;
    private String className;
    static {
        logconf = loadLogConfiguration();
    }

    public CTOMSLogger(String className) {
        this.className = className;
    }

    /**
     * 消息日志
     */
    public void log(LogLevel level, String msg) {
        org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(className);
        log.setLevel(Level.toLevel(logconf.getRoot().getLevel()));
        List<String> handlerList = logconf.getRoot().getHandlers();
        if (logconf.getFile() != null) {
            if (handlerList.contains(logconf.getFile().getId())) {
                if (log.getAppender("fileappender") == null) {
                    DailyRollingFileAppender fileAppender = new DailyRollingFileAppender();
                    fileAppender.setName("fileappender");
                    fileAppender.setAppend(logconf.getFile().isAppend());

                    if (logconf.getFile().getFormatPattern() != null) {
                        fileAppender.setLayout(new PatternLayout(logconf.getFile().getFormatPattern()));
                    }
                    if (logconf.getFile().getFilePath() != null) {
                        fileAppender.setFile(logconf.getFile().getFilePath());
                    }
                    if (logconf.getFile().getSuffix() != null) {
                        fileAppender.setDatePattern(logconf.getFile().getSuffix());
                    }
                    if (logconf.getFile().getLevel() != null) {
                        fileAppender.setThreshold(Level.toLevel(logconf.getFile().getLevel()));
                    }
                    fileAppender.setImmediateFlush(true);
                    fileAppender.activateOptions();
                    log.addAppender(fileAppender);
                }
            }
        }

        if (logconf.getConsole() != null) {
            if (handlerList.contains(logconf.getConsole().getId())) {
                if (log.getAppender("consoleappender") == null) {
                    ConsoleAppender consoleAppender = new ConsoleAppender();
                    log.setAdditivity(false);
                    consoleAppender.setName("consoleappender");
                    if (logconf.getConsole().getFormatPattern() != null) {
                        consoleAppender.setLayout(new PatternLayout(logconf.getConsole().getFormatPattern()));
                    }
                    if (logconf.getConsole().getLevel() != null) {
                        consoleAppender.setThreshold(Level.toLevel(logconf.getConsole().getLevel()));
                    }
                    consoleAppender.activateOptions();
                    log.addAppender(consoleAppender);
                }
            }
        }
        log.log(Level.toLevel(level.value),msg);
    }

    /**
     * 用于记录操作日志，结果按配置存入表同时按照格式存入文件中表中
     */
//    public void record(Log ctomsLog) {
//        try {
//            if (ctomsLog instanceof CTOMSLog) {
//                CTOMSLog log = (CTOMSLog) ctomsLog;
//                String[] result = { "成功", "失败" };
//                String msg = "操作者：" + log.getOperator() + "\t操作IP：" + log.getOperatorIP() + "\t操作结果："
//                        + result[log.getResult().value] + "\t日志内容：" + log.getContent();
//                log(LogLevel.INFO,msg);
//
//                List<String> handlerList = logconf.getRoot().getHandlers();
//                if (logconf.getDb() != null) {
//                    if (handlerList.contains(logconf.getDb().getId())) {
//                        CTOMSOpLog opLog = new CTOMSOpLog();
//                        UUID id = new UUID();
//                        opLog.setId(id.toString());
//                        opLog.setOpContent(log.getContent());
//                        opLog.setOperator(log.getOperator());
//                        opLog.setOpIp(log.getOperatorIP());
//                        opLog.setOpTime(System.currentTimeMillis());
//                        opLog.setOpResult(log.getResult().value);
//                        opLog.setOpType(log.getOperationType().value);
//                        CTOMSLogDAO logDAO = (CTOMSLogDAO)ServiceContext.getInstance().getService("CTOMSLogDAO");
//                        logDAO.addLog(opLog);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private static CTOMSLoggerConfig loadLogConfiguration() {
        if (logconf == null) {
            CTOMSLoggerConfig conf = new CTOMSLoggerConfig();
            CTOMSLoggerConfig.ConsoleLogger consoleLogger = null;
            CTOMSLoggerConfig.FileLogger fileLogger = null;
            CTOMSLoggerConfig.DBLogger dbLogger = null;
            CTOMSLoggerConfig.RootLogger rootLogger = null;
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = dbf.newDocumentBuilder();
                Document doc = builder.parse(CTOMSLogger.class.getClassLoader().getResourceAsStream("log.config.xml"));
                NodeList nodeList = doc.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node root = nodeList.item(i);
                    if (root.getNodeName().equals("log-system")) {
                        NodeList contentNodeList = root.getChildNodes();
                        for (int index = 0; index < contentNodeList.getLength(); index++) {
                            Node ele = contentNodeList.item(index);
                            // Console
                            if (ele.getNodeName().equals("console-handler")) {
                                consoleLogger = conf.new ConsoleLogger();
                                NamedNodeMap attr = ele.getAttributes();
                                for (int at1 = 0; at1 < attr.getLength(); at1++) {
                                    if (attr.item(at1).getNodeName().equals("name")) {
                                        consoleLogger.setId(attr.item(at1).getNodeValue());
                                    }
                                }
                                NodeList consoleNodeList = ele.getChildNodes();
                                for (int c = 0; c < consoleNodeList.getLength(); c++) {
                                    Node content = consoleNodeList.item(c);
                                    if (content.getNodeName().equals("level")) {
                                        NamedNodeMap levelAttrs = content.getAttributes();
                                        for (int levelIdx = 0; levelIdx < levelAttrs.getLength(); levelIdx++) {
                                            if (levelAttrs.item(levelIdx).getNodeName().equals("name")) {
                                                consoleLogger.setLevel(levelAttrs.item(levelIdx).getNodeValue());
                                            }
                                        }
                                    }
                                    if (content.getNodeName().equals("formatter")) {
                                        NodeList formatterNodeList = content.getChildNodes();
                                        for (int formatterIdx = 0; formatterIdx < formatterNodeList.getLength(); formatterIdx++) {
                                            Node pattern = formatterNodeList.item(formatterIdx);
                                            if (pattern.getNodeName().equalsIgnoreCase("pattern-formatter")) {
                                                NamedNodeMap patternAttrs = pattern.getAttributes();
                                                for (int patternIdx = 0; patternIdx < patternAttrs.getLength(); patternIdx++) {
                                                    consoleLogger.setFormatPattern(patternAttrs.item(patternIdx)
                                                            .getNodeValue());
                                                }
                                            }
                                        }
                                    }
                                }
                                conf.setConsole(consoleLogger);
                            }
                            // File
                            if (ele.getNodeName().equals("file-handler")) {
                                fileLogger = conf.new FileLogger();
                                NamedNodeMap attr = ele.getAttributes();
                                for (int at1 = 0; at1 < attr.getLength(); at1++) {
                                    if (attr.item(at1).getNodeName().equals("name")) {
                                        fileLogger.setId(attr.item(at1).getNodeValue());
                                    }
                                }
                                NodeList fileNodeList = ele.getChildNodes();
                                for (int c = 0; c < fileNodeList.getLength(); c++) {
                                    Node content = fileNodeList.item(c);
                                    if (content.getNodeName().equals("level")) {
                                        NamedNodeMap levelAttrs = content.getAttributes();
                                        for (int levelIdx = 0; levelIdx < levelAttrs.getLength(); levelIdx++) {
                                            if (levelAttrs.item(levelIdx).getNodeName().equals("name")) {
                                                fileLogger.setLevel(levelAttrs.item(levelIdx).getNodeValue());
                                            }
                                        }
                                    }
                                    if (content.getNodeName().equals("formatter")) {
                                        NodeList formatterNodeList = content.getChildNodes();
                                        for (int formatterIdx = 0; formatterIdx < formatterNodeList.getLength(); formatterIdx++) {
                                            Node pattern = formatterNodeList.item(formatterIdx);
                                            if (pattern.getNodeName().equalsIgnoreCase("pattern-formatter")) {
                                                NamedNodeMap patternAttrs = pattern.getAttributes();
                                                for (int patternIdx = 0; patternIdx < patternAttrs.getLength(); patternIdx++) {
                                                    fileLogger.setFormatPattern(patternAttrs.item(patternIdx)
                                                            .getNodeValue());
                                                }
                                            }
                                        }
                                    }
                                    if (content.getNodeName().equals("log-file")) {
                                        String relativePath = "";
                                        String filePath = "";
                                        NamedNodeMap logFileAttrs = content.getAttributes();
                                        for (int attrIdx = 0; attrIdx < logFileAttrs.getLength(); attrIdx++) {
                                            if (logFileAttrs.item(attrIdx).getNodeName().equals("relative-to")) {
                                                relativePath = logFileAttrs.item(attrIdx).getNodeValue();
                                                if (System.getenv(relativePath) != null) {
                                                    relativePath = System.getenv(relativePath);
                                                }
                                            }
                                            if (logFileAttrs.item(attrIdx).getNodeName().equals("path")) {
                                                filePath = logFileAttrs.item(attrIdx).getNodeValue();
                                            }
                                        }
                                        if (relativePath != null) {
                                            if (relativePath.endsWith(File.separator)) {
                                                relativePath = relativePath.substring(0,relativePath.length() - 2);
                                            }
                                        }
                                        if (filePath != null) {
                                            if (filePath.startsWith(File.separator)) {
                                                filePath = filePath.substring(0,filePath.length() - 2);
                                            }
                                        }
                                        if (relativePath.length() == 0) {
                                            fileLogger.setFilePath(filePath);
                                        } else {
                                            fileLogger.setFilePath(relativePath + File.separator + filePath);
                                        }
                                    }
                                    if (content.getNodeName().equals("suffix")) {
                                        NamedNodeMap attrs = content.getAttributes();
                                        for (int attrIdx = 0; attrIdx < attrs.getLength(); attrIdx++) {
                                            if (attrs.item(attrIdx).getNodeName().equals("value")) {
                                                fileLogger.setSuffix(attrs.item(attrIdx).getNodeValue());
                                            }
                                        }
                                    }
                                    if (content.getNodeName().equals("append")) {
                                        NamedNodeMap attrs = content.getAttributes();
                                        for (int attrIdx = 0; attrIdx < attrs.getLength(); attrIdx++) {
                                            if (attrs.item(attrIdx).getNodeName().equals("value")) {
                                                fileLogger.setAppend(attrs.item(attrIdx).getNodeValue().equals("true"));
                                            }
                                        }
                                    }
                                }
                                conf.setFile(fileLogger);
                            }
                            // Database
                            if (ele.getNodeName().equals("db-handler")) {
                                dbLogger = conf.new DBLogger();
                                NamedNodeMap attr = ele.getAttributes();
                                for (int at1 = 0; at1 < attr.getLength(); at1++) {
                                    if (attr.item(at1).getNodeName().equals("name")) {
                                        dbLogger.setId(attr.item(at1).getNodeValue());
                                    }
                                }
                                NodeList dbNodeList = ele.getChildNodes();
                                java.util.Properties dbProp = new java.util.Properties();
                                for (int c = 0; c < dbNodeList.getLength(); c++) {
                                    Node content = dbNodeList.item(c);
                                    if (content.getNodeName().equals("properties")) {
                                        NodeList propertiesNodeList = content.getChildNodes();
                                        for (int nodeIdx = 0; nodeIdx < propertiesNodeList.getLength(); nodeIdx++) {
                                            Node prop = propertiesNodeList.item(nodeIdx);
                                            String key = null, value = null;
                                            if (prop.getNodeName().equals("property")) {
                                                NamedNodeMap propAttrs = prop.getAttributes();
                                                for (int attrIdx = 0; attrIdx < propAttrs.getLength(); attrIdx++) {
                                                    if (propAttrs.item(attrIdx).getNodeName().equals("name")) {
                                                        key = propAttrs.item(attrIdx).getNodeValue();
                                                    }
                                                    if (propAttrs.item(attrIdx).getNodeName().equals("value")) {
                                                        value = propAttrs.item(attrIdx).getNodeValue();
                                                    }
                                                }
                                                dbProp.put(key,value);
                                            }
                                            dbLogger.setDbProperties(dbProp);
                                        }
                                    }
                                }
                                conf.setDb(dbLogger);
                            }
                            // Common
                            if (ele.getNodeName().equals("root-logger")) {
                                rootLogger = conf.new RootLogger();
                                NodeList dbNodeList = ele.getChildNodes();
                                for (int c = 0; c < dbNodeList.getLength(); c++) {
                                    Node content = dbNodeList.item(c);
                                    if (content.getNodeName().equals("level")) {
                                        NamedNodeMap attrs = content.getAttributes();
                                        for (int attrIdx = 0; attrIdx < attrs.getLength(); attrIdx++) {
                                            if (attrs.item(attrIdx).getNodeName().equals("name")) {
                                                rootLogger.setLevel(attrs.item(attrIdx).getNodeValue());
                                            }
                                        }
                                    }
                                    if (content.getNodeName().equals("handlers")) {
                                        NodeList propertiesNodeList = content.getChildNodes();
                                        java.util.List<String> handlerList = new java.util.ArrayList<String>();
                                        for (int nodeIdx = 0; nodeIdx < propertiesNodeList.getLength(); nodeIdx++) {
                                            Node handlerNode = propertiesNodeList.item(nodeIdx);
                                            if (handlerNode.getNodeName().equals("handler")) {
                                                NamedNodeMap handlerAttrs = handlerNode.getAttributes();
                                                for (int attrIdx = 0; attrIdx < handlerAttrs.getLength(); attrIdx++) {
                                                    if (handlerAttrs.item(attrIdx).getNodeName().equals("name")) {
                                                        handlerList.add(handlerAttrs.item(attrIdx).getNodeValue());
                                                    }
                                                }
                                            }
                                        }
                                        rootLogger.setHandlers(handlerList);
                                    }
                                }
                                conf.setRoot(rootLogger);
                            }
                        }

                    }
                }
                logconf = conf;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return logconf;
    }
}
