package org.demon.taole.pojo;

import java.util.Date;

public class Commodity {
    private Integer id;

    private Integer taskId;

    private String productId;

    private String name;

    private Integer price;

    private Integer lowPrice;

    private Integer sendPrice;

    private String url;

    private Date createtime;

    private Date updatetime;

    private Date lowtime;

    private String asdUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId == null ? null : productId.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Integer lowPrice) {
        this.lowPrice = lowPrice;
    }

    public Integer getSendPrice() {
        return sendPrice;
    }

    public void setSendPrice(Integer sendPrice) {
        this.sendPrice = sendPrice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Date getLowtime() {
        return lowtime;
    }

    public void setLowtime(Date lowtime) {
        this.lowtime = lowtime;
    }

    public String getAsdUrl() {
        return asdUrl;
    }

    public void setAsdUrl(String asdUrl) {
        this.asdUrl = asdUrl;
    }
}