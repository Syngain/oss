package com.guanhuodata.framework.util;

public class Page implements java.io.Serializable{
    private static final long serialVersionUID = 1705513846045183388L;
    private int startPage;
    private int pageSize;
    public int getStartPage() {
        return startPage;
    }
    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
