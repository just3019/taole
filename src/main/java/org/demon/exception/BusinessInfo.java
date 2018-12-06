package org.demon.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-05 16:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessInfo {

    /**
     * 未知错误
     */
    public static final BusinessInfo UNKNOW = new BusinessInfo(-1, "未知错误");
    /**
     * 成功
     */
    public static final BusinessInfo SUCCESS = new BusinessInfo(1000, "成功");


    private int code;
    private String msg;


}
