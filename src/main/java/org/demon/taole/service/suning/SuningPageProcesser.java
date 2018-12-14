package org.demon.taole.service.suning;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.demon.taole.pojo.ScanProduct;
import org.demon.taole.pojo.SuningTask;
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
@CommonsLog
public class SuningPageProcesser implements PageProcessor {

    private Site site;

    private SuningTask suningTask;

    private ScanProductService scanProductService;

    public SuningPageProcesser(SuningTask suningTask, ScanProductService scanProductService) {
        this.suningTask = suningTask;
        this.scanProductService = scanProductService;
    }

    public SuningPageProcesser() {
    }


    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        String name = html.xpath("//*[@id=\"itemDisplayName\"]/text()").toString();
        String price = page.getHtml().xpath(suningTask.getXpathPrice()).toString();
        ScanProduct scanProduct = new ScanProduct();
        scanProduct.setPrice(NumberUtil.parseInt(price));
        scanProduct.setName(name);
        scanProduct.setProductId(suningTask.getProductId());
        scanProduct.setSource("1");
        if (scanProduct.getPrice() == 0 || scanProduct.getPrice() == null) {
            log.info(StrUtil.format("\n{}\n{}", name, price));
            return;
        }
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
