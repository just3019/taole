package org.demon.taole;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import org.junit.Test;

import java.util.Date;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-18 16:56
 */
public class JavaTest {

    @Test
    public void test(){
        System.out.println(DateUtil.beginOfDay(DateUtil.yesterday()));
        System.out.println(DateUtil.endOfDay(DateUtil.yesterday()));
    }
}
