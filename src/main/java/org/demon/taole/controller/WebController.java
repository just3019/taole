package org.demon.taole.controller;

import org.demon.taole.bean.CommodityQuery;
import org.demon.taole.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

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


    @GetMapping("/web/commodities/{taskId}/{page}")
    public String commodities(ModelMap map, @PathVariable("taskId") Integer taskId, @PathVariable("page") Integer page) {
        CommodityQuery query = new CommodityQuery();
        query.taskId = Optional.ofNullable(taskId).orElse(0);
        query.setPage(Optional.ofNullable(page).orElse(1));
        query.orderBy = " updatetime desc ";
        map.addAttribute("commodities", commodityService.select(query));
        return "product/commodity";
    }
}
