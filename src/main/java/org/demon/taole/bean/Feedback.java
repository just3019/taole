package org.demon.taole.bean;

import java.util.List;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-19 17:23
 */
public class Feedback {
    public Integer taskId;
    public String url;
    public String lowPrice;
    public String price;
    public String originalPrice;
    public String name;
    public String productId;
    public List<FeedbackPrice> feedbackPrices;
}
