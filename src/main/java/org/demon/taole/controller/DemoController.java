package org.demon.taole.controller;

import org.demon.taole.pojo.Demo;
import org.demon.taole.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-05 14:32
 */
@RestController
public class DemoController {

    @Autowired
    private DemoService demoService;


    @GetMapping(value = "demo/{id}")
    public Demo test_01(@PathVariable("id") Long id) {
        return demoService.get(id);
    }

    @PostMapping(value = "demo")
    public Demo test_02(@RequestBody Demo demo) {
        return demoService.get(demo.getId());
    }
}
