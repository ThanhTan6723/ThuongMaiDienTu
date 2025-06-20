package model;

import dao.client.LOG_LEVEL;
import model.Account;

public class Log {
    private int id;
    private String timesTamp;
    private LOG_LEVEL logLevel;
    private String module;
    private String actionType;
    private Account account;
    private String logContent;
    private String sourceIP;
    private String userAgent;
    private String affectedTable;
    private String beforeData;
    private String afterData;
    private String national;

    public Log() {
    }

    public Log(int id, String timesTamp, LOG_LEVEL logLevel, String module, String actionType, Account account, String logContent, String sourceIP, String userAgent, String affectedTable, String beforeData, String afterData, String national) {
        this.id = id;
        this.timesTamp = timesTamp;
        this.logLevel = logLevel;
        this.module = module;
        this.actionType = actionType;
        this.account = account;
        this.logContent = logContent;
        this.sourceIP = sourceIP;
        this.userAgent = userAgent;
        this.affectedTable = affectedTable;
        this.beforeData = beforeData;
        this.afterData = afterData;
        this.national = national;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimesTamp() {
        return timesTamp;
    }

    public void setTimesTamp(String timesTamp) {
        this.timesTamp = timesTamp;
    }

    public LOG_LEVEL getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LOG_LEVEL logLevel) {
        this.logLevel = logLevel;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public void setSourceIP(String sourceIP) {
        this.sourceIP = sourceIP;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getAffectedTable() {
        return affectedTable;
    }

    public void setAffectedTable(String affectedTable) {
        this.affectedTable = affectedTable;
    }

    public String getBeforeData() {
        return beforeData;
    }

    public void setBeforeData(String beforeData) {
        this.beforeData = beforeData;
    }

    public String getAffterData() {
        return afterData;
    }

    public void setAffterData(String affterData) {
        this.afterData = affterData;
    }

    public String getAfterData() {
        return afterData;
    }

    public void setAfterData(String afterData) {
        this.afterData = afterData;
    }

    public String getNational() {
        return national;
    }

    public void setNational(String national) {
        this.national = national;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", timesTamp='" + timesTamp + '\'' +
                ", logLevel=" + logLevel +
                ", module='" + module + '\'' +
                ", actionType='" + actionType + '\'' +
                ", account=" + account +
                ", logContent='" + logContent + '\'' +
                ", sourceIP='" + sourceIP + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", affectedTable='" + affectedTable + '\'' +
                ", beforeData='" + beforeData + '\'' +
                ", afterData='" + afterData + '\'' +
                ", national='" + national + '\'' +
                '}';
    }
}



