package org.demon.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResult<T> {
    private Integer status;
    private String msg;
    private T data;

    public BaseResult(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }


}
