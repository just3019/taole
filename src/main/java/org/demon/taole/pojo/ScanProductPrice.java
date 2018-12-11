package org.demon.taole.pojo;

import java.util.Date;

public class ScanProductPrice {
    private Long id;

    private Long scanProductId;

    private Integer price;

    private Date createtime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScanProductId() {
        return scanProductId;
    }

    public void setScanProductId(Long scanProductId) {
        this.scanProductId = scanProductId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}