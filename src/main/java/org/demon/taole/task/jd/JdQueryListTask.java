package org.demon.taole.task.jd;

import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.NumberUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.apachecommons.CommonsLog;
import org.demon.taole.pojo.ScanProduct;
import org.demon.taole.service.ScanProductService;
import org.demon.util.FunctionUtil;
import org.demon.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-06 16:48
 */
@CommonsLog
public class JdQueryListTask implements Runnable {

    private ScanProductService scanProductService;

    @Autowired
    public JdQueryListTask(ScanProductService scanProductService) {
        this.scanProductService = scanProductService;
    }


    @Override
    public void run() {
        Body body = new Body();
        body.setKeyword("iphone xs max");
        Price price = new Price("6000", "10000");
        body.setPrice(price);
        JdSearch jdSearch = new JdSearch();
        jdSearch.setBody(JSONUtil.obj2Json(body));
        String params = jdSearch.toParams();
        System.out.println(params);
        HttpClient.newBuilder().build().sendAsync(
                HttpRequest.newBuilder().uri(URI.create("https://api.m.jd.com/client.action?functionId=search"))
                        .header("content-type", "application/x-www-form-urlencoded")
                        .POST(HttpRequest.BodyPublishers.ofString(params)).build(),
                HttpResponse.BodyHandlers.ofString()
        ).whenComplete((resp, e) -> {
            FunctionUtil.whenTrueDo(Objects::nonNull, Throwable::printStackTrace, e);
            //            log.info("body:" + resp.body());
            Result result = JSONUtil.json2Obj(resp.body(), Result.class);
            assert result != null;
            result.wareInfo.forEach(a -> scanProductService.save(convert(a)));
        }).join();
    }

    private ScanProduct convert(WareInfo a) {
        ScanProduct scanProduct = new ScanProduct();
        scanProduct.setName(a.wname);
        scanProduct.setProductId(a.wareId);
        scanProduct.setSource("0");
        scanProduct.setPrice(NumberUtil.parseInt(a.jdPrice));
        return scanProduct;
    }


    @Data
    private class Result {
        private List<WareInfo> wareInfo;

    }


    @Data
    private class WareInfo {
        //        private String spuId;
        //        private String cid1;
        //        private String cid2;
        //        private String catid;
        private String wareId;
        private String wname;
        //        private String imageurl;
        private String jdPrice;
        //        private List<String> promotionFlag;
        private long totalCount;
        //        private String fuzzyTotalCount;
        //        private boolean recentYear;
        //        private String reviews;
        //        private String venderId;
        //        private String venderType;
        //        private String shopId;
        //        private boolean eBookFlag;
        //        private boolean international;
        //        private boolean eche;
        //        private boolean needShield;
        //        private boolean needShieldCartAndFollow;
        //        private List<String> promotionType;
        //        private String showlog;
        //        private String flags;
        //        private boolean groupBuyFlag;
        //        private List<String> customAttrList;
        //        private String author;
        //        private boolean flashBuy;
        //        private String promotionIconUrl;
        //        private String longImgUrl;
        //        private boolean preSale;
        //        private boolean purchasing;
        private String couponTag;
        //        private boolean exposeSkus;
        //        private int colorCount;
        //        private boolean newSeasonClothes;
        //        private boolean localStore;
        //        private String installment;
        //        private String priceType;
        //        private String videoIconUrl;
        //        private boolean secKill;
        //        private boolean loc;
        //        private boolean self;
    }


    @Data
    public class Body {
        private String isCorrect = "1";
        private String showStoreTab = "1";
        private String orignalSelect = "0";
        private String insertedCount = "0";
        private String keyword = "iphone";
        private String pagesize = "10";
        private String sort = "3";//3:排序价格升序
        private String orignalSearch = "0";
        private String stock = "1";
        private String newMiddleTag = "1";
        private String articleEssay = "1";
        private String latitude = "30.294477";
        private String oneBoxMod = "1";
        private String deviceidTail = "85";
        private String jshop = "1";
        private String addrFilter = "1";
        private String insertArticle = "1";
        private String longitude = "120.118829";
        private String newVersion = "3";
        private String lastkey = "纸巾";
        private String pageEntrance = "1";
        private String page = "1";
        private Price price;
        private String insertScene = "1";
        private String showShopTab = "yes";

    }

    @Data
    @AllArgsConstructor
    private class Price {
        private String min;
        private String max;

    }

    @Data
    public class JdSearch {
        private String adid = "8D49B19A-F460-4302-9902-5374CBB0AE45";
        private String area = "15_1213_3411_52667";
        private String body;
        private String build = "164842";
        private String client = "apple";
        private String clientVersion = "7.2.6";
        private String openudid = "f9548741c2c64dc34cecf450543d893a3a8cd415";
        private String sign = "672d8f060f2a3b737123fb2f6fa2d3f6";
        private String st = "1544521883962";
        private String sv = "122";
        private String uuid = "coW0lj7vbXVin6h7ON+tMNFQqYBqMahr";

        String toParams() {
            return StrFormatter.format("body={}&client={}&clientVersion={}&openudid={}&sign={}&st={}&sv={}", body,
                    client, clientVersion, openudid, sign, st, sv);
        }

    }
}
