package org.demon.taole.controller;

import cn.hutool.json.JSONUtil;
import org.demon.taole.ScanBean;
import org.demon.taole.service.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-06 17:01
 */
@RestController
public class ScanController {

    @Autowired
    private ScanService scanService;


    @PostMapping(value = "scan")
    public void save(@RequestBody ScanBean bean) {
        System.out.println(JSONUtil.toJsonPrettyStr(bean));
        scanService.save(bean);
    }

    @PostMapping(value = "scan/batch")
    public void saveBatch(@RequestBody List<ScanBean> beans) {
        scanService.saveBatch(beans);
    }
}
