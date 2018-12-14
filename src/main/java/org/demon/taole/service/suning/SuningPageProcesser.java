package org.demon.taole.service.suning;

import cn.hutool.core.util.NumberUtil;
import org.demon.taole.pojo.ScanProduct;
import org.demon.taole.service.ScanProductService;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-14 10:50
 */
@Component
public class SuningPageProcesser implements PageProcessor {

    private Site site;

    private String productId;

    private ScanProductService scanProductService;

    public SuningPageProcesser(String productId, ScanProductService scanProductService) {
        this.productId = productId;
        this.scanProductService = scanProductService;
    }

    public SuningPageProcesser() {

    }


    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        String name = html.xpath("//*[@id=\"itemDisplayName\"]/text()").toString();
        String price = page.getHtml().xpath("//*[@id=\"mainPrice\"]/dl[1]/dd/span[1]/text()").toString();
        ScanProduct scanProduct = new ScanProduct();
        scanProduct.setPrice(NumberUtil.parseInt(price));
        scanProduct.setName(name);
        scanProduct.setProductId(productId);
        scanProduct.setSource("1");
        scanProductService.save(scanProduct);
    }

    @Override
    public Site getSite() {
        if (site == null) {
            site = Site.me().setRetryTimes(3).setSleepTime(1000);
        }
        return site;
    }
}