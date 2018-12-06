package org.demon.bean;

import lombok.Data;

import java.util.List;

/**
 * @author demon
 * @version 1.0
 * @date 2018/10/18 17:41
 * @since 1.0
 */
@Data
public class PageData<T> {
    public long count = 0;
    public List<T> list;
}
