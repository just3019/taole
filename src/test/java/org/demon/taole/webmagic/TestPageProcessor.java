package org.demon.taole.webmagic;

import cn.hutool.core.util.StrUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.demon.pool.SeleniumDownloader;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.sql.Struct;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-13 09:28
 */
@CommonsLog
public class TestPageProcessor implements PageProcessor {

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site;

    public static void main(String[] args) {
//        Spider.create(new TestPageProcessor())
//                .addUrl("https://product.suning.com/0000000000/10606649860.html")
//                .setDownloader(new SeleniumDownloader())
//                .thread(5).run();
        Spider.create(new TestPageProcessor())
                .addUrl("https://search.suning.com/iphonexsmax/")
                .setDownloader(new SeleniumDownloader())
                .thread(5).run();
    }

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        log.info(html.xpath("//*[@id=\"0000000000-10606649860\"]"));
        String name = html.xpath("//*[@id=\"itemDisplayName\"]/text()").toString();
        String price = page.getHtml().xpath("//*[@id=\"mainPrice\"]/dl[1]/dd/span[1]/text()").toString();
        log.info(StrUtil.format("name:{},\nprice:{}", name, price));
    }

    @Override
    public Site getSite() {
        if (site == null) {
            site = Site.me().setRetryTimes(3).setSleepTime(1000);
        }
        return site;
    }
}
