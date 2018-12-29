package org.demon.taole.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-29 14:28
 */
public class Constants {

    //排序方式
    public static Map<Integer, String> MAP = new HashMap<>();

    static {
        MAP.put(1, " price asc ");
        MAP.put(2, " percent asc ");
        MAP.put(3, " lowtime desc");
        MAP.put(4, " updatetime desc ");
    }
}
