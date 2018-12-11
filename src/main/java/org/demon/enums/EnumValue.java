package org.demon.enums;

/**
 * Description:  枚举类获取值
 * Created by Sean.xie on 2017/3/15.
 */
public interface EnumValue<T> {
    /**
     * 取真实值
     */
    T getValue();

    /**
     * 枚举名称
     */
    String name();
}
