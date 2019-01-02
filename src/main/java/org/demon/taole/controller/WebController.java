package org.demon.taole.controller;

import org.demon.bean.PageData;
import org.demon.exception.BusinessException;
import org.demon.taole.bean.CommodityQuery;
import org.demon.taole.bean.Constants;
import org.demon.taole.bean.EmailQuery;
import org.demon.taole.pojo.Commodity;
import org.demon.taole.service.CommodityPriceService;
import org.demon.taole.service.CommodityService;
import org.demon.taole.service.EmailService;
import org.demon.util.FunctionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    private CommodityPriceService commodityPriceService;
    @Autowired
    private EmailService emailService;

    @GetMapping("/web/goods/{page}")
    public String commodities(ModelMap map, @PathVariable("page") Integer page, Integer taskId, String name,
                              Integer size, Integer orderBy, String platform) {
        CommodityQuery query = new CommodityQuery();
        query.taskId = Optional.ofNullable(taskId).orElse(0);
        query.setPage(Optional.ofNullable(page).orElse(1));
        query.name = Optional.ofNullable(name).orElse(null);
        query.platform = platform;
        FunctionUtil.whenNonNullDo(query::setSize, size);
        query.orderBy = Constants.MAP.get(Optional.ofNullable(orderBy).orElse(4));
        PageData<Commodity> pageData = commodityService.select(query);
        pageData.list = pageData.list.stream().peek(this::convertAsd).collect(Collectors.toList());
        map.addAttribute("goods", pageData);
        map.addAttribute("size", Optional.ofNullable(size).orElse(query.getSize()));
        map.addAttribute("name", Optional.ofNullable(name).orElse(""));
        map.addAttribute("taskId", query.taskId);
        map.addAttribute("offset", page);
        map.addAttribute("orderBy", Optional.ofNullable(orderBy).orElse(4));
        map.addAttribute("platform", platform);
        return "product/goods";
    }

    private void convertAsd(Commodity commodity) {
        commodity.setAsdUrl(commodity.getUrl()
                .replaceFirst("amazon", "amazonasd")
                .replaceFirst("jd", "jdasd")
                .replaceFirst("suning", "suningasd")
                .replaceFirst("kaola", "kaolaasd"));
    }

    @GetMapping("/web/goods/sendPrice/{commodityId}/{sendPrice}")
    @ResponseBody
    public void updateGoods(@PathVariable("commodityId") Integer commodityId,
                            @PathVariable("sendPrice") Integer sendPrice) {
        FunctionUtil.check(commodityId == null || commodityId < 1, new BusinessException(-2, "参数错误"));
        commodityService.update(commodityId, sendPrice);
    }

    @GetMapping("/web/goods/noSelect/{commodityId}")
    @ResponseBody
    public void noSelect(@PathVariable("commodityId") Integer commodityId) {
        commodityService.noSelect(commodityId);
    }

    @GetMapping("/web/stat/{commodityId}")
    public String stat(ModelMap map, @PathVariable("commodityId") Integer commodityId) {
        map.addAttribute("goods", commodityService.select(commodityId));
        map.addAttribute("prices", commodityPriceService.select(commodityId));
        return "product/stat";
    }

    @GetMapping("/web/emails/{page}")
    public String emails(ModelMap map, @PathVariable("page") Integer page) {
        EmailQuery query = new EmailQuery();
        query.setPage(Optional.ofNullable(page).orElse(1));
        map.addAttribute("emails", emailService.select(query));
        return "email/emails.html";
    }
}
