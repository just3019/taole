package org.demon.taole.bean;

import org.demon.bean.PageReq;

import java.util.List;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-18 12:54
 */
public class TaskQuery extends PageReq {

    public List<Integer> ids;
    public Integer uid;
    public String status;//状态 0关闭 1开启
    public String type; //类型 1url 2keyword
    public String platform; //平台号 0京东  1苏宁
}
