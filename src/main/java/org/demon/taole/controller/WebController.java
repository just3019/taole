package org.demon.taole.controller;

import org.demon.taole.bean.CommodityQuery;
import org.demon.taole.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-20 12:47
 */
@Controller
public class WebController {

    @Autowired
    private CommodityService commodityService;


    @GetMapping("/web/commodities")
    public String commodities(ModelMap map){
        map.addAttribute("commodities", commodityService.select(new CommodityQuery()));
        return "product/commodity";
    }
}
