package com.duhc.mybatis.entity;

/**
 * Created by duhc on 2017/7/28.
 */
public class Pager {
    private int pagesize;
    private int pageid;
    private int pagebegin;
    private int count;

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getPageid() {
        return pageid;
    }

    public void setPageid(int pageid) {
        this.pageid = pageid;
    }

    public int getPagebegin() {
        return pagebegin;
    }

    public void setPagebegin(int pagebegin) {
        this.pagebegin = pagebegin;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
