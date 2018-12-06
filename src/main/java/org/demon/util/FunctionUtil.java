package org.demon.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * desc: 方法工具
 *
 * @author demon
 * @date 2018-11-21 17:14
 */
public class FunctionUtil {

    /**
     * 当 val 值存在的时候，执行传入的方法，否则不处理
     * If a value is present, performs the given action with the value,
     * otherwise does nothing
     *
     * @param c   处理方法
     * @param val 值
     */
    public static <T> void whenNonNullDo(Consumer<T> c, T val) {
        Optional.ofNullable(val).ifPresent(c);
    }

    /**
     * 当验证为成功后执行，不成功不执行
     *
     * @param p   {@link Predicate}
     * @param c   需要执行的方法{@link Consumer}
     * @param val 需要处理的值
     */
    public static <T> void whenTrueDo(Predicate<T> p, Consumer<T> c, T val) {
        if (p.test(val)) {
            c.accept(val);
        }
    }


    /**
     * 通过flag判断是否执行方法，
     *
     * @param flag 是否执行
     * @param s    需要执行的方法 {@link Supplier}
     */
    public static <T> T whenTrueDoReturn(boolean flag, Supplier<T> s) {
        if (flag) {
            return s.get();
        }
        return null;
    }


    /**
     * flag为true ，抛错
     *
     * @param flag      false， true
     * @param exception {@link Exception}
     */
    public static <T extends Exception> void check(boolean flag, T exception) throws T {
        if (flag) {
            throw exception;
        }
    }

}
