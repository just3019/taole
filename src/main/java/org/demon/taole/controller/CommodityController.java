package org.demon.taole.controller;

import org.demon.bean.PageData;
import org.demon.taole.bean.CommodityQuery;
import org.demon.taole.pojo.Commodity;
import org.demon.taole.pojo.CommodityPrice;
import org.demon.taole.service.CommodityService;
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
 * @date 2018-12-18 14:00
 */
@RestController
public class CommodityController {

    @Autowired
    private CommodityService commodityService;

    @PostMapping(value = "/commodities")
    public PageData<Commodity> select(@RequestBody CommodityQuery query) {
        return commodityService.select(query);
    }


}
