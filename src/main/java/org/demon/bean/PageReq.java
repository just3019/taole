package org.demon.bean;

/**
 * @author demon
 * @version 1.0
 * @date 2018/10/18 17:43
 * @since 1.0
 */
public class PageReq {
    private Integer page;
    private Integer size;
    private Boolean isFull;

    public int getLimit() {
        if (isFull()) {
            return 0;
        }
        return getSize() > 100 ? 100 : getSize();
    }

    public int getOffset() {
        if (isFull()) {
            return 0;
        }
        return ((getPage() - 1) < 0 ? 0 : getPage() - 1) * getSize();
    }

    public Integer getPage() {
        return page == null ? 1 : page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size == null ? 30 : size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Boolean isFull() {
        return this.isFull == null ? false : this.isFull;
    }

    public void setFull(Boolean full) {
        this.isFull = full;
    }
}
