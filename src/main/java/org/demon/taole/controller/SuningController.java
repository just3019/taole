package org.demon.taole.controller;

import org.demon.taole.bean.Suning;
import org.demon.taole.service.suning.SuningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-14 10:45
 */
@RestController
public class SuningController {

    @Autowired
    private SuningService suningService;

    @PostMapping(value = "/add/url")
    public void addUrl(@RequestBody Suning suning) {
        suningService.addUrl(suning);
    }
}
