package org.demon.taole.bean;

import java.util.List;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-18 14:35
 */
public class TaskFeedback {

    public List<Feedback> feedbacks;

    public class Feedback {
        public Integer taskId;
        public String url;
        public Integer lowPrice;
        public Integer price;
        public String name;
        public String productId;
        public List<FeedbackPrice> feedbackPrices;
    }

    public class FeedbackPrice {
        public Integer price;
    }
}
