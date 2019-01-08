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
        MAP.put(5, " low_price asc ");
    }

    public static String convertAsd(String url) {
        String asdUrl = url.replaceFirst("gome", "gomeasd").replaceFirst("amazon", "amazonasd")
                .replaceFirst("jd", "jdasd").replaceFirst("suning", "suningasd")
                .replaceFirst("kaola", "kaolaasd");
        return asdUrl;

    }
}
