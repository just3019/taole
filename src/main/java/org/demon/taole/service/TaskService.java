package org.demon.taole.service;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.demon.bean.PageData;
import org.demon.exception.BusinessException;
import org.demon.pool.ExecutorPool;
import org.demon.taole.bean.Feedback;
import org.demon.taole.bean.FeedbackPrice;
import org.demon.taole.bean.TaskFeedback;
import org.demon.taole.bean.TaskQuery;
import org.demon.taole.mapper.CommodityMapper;
import org.demon.taole.mapper.CommodityPriceMapper;
import org.demon.taole.mapper.TaskMapper;
import org.demon.taole.pojo.Commodity;
import org.demon.taole.pojo.CommodityExample;
import org.demon.taole.pojo.CommodityPrice;
import org.demon.taole.pojo.Task;
import org.demon.taole.pojo.TaskExample;
import org.demon.util.FunctionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-18 11:55
 */
@Service
@CommonsLog
public class TaskService {

    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private CommodityMapper commodityMapper;
    @Autowired
    private CommodityPriceMapper commodityPriceMapper;
    @Autowired
    private MailService mailService;


    public Task save(Task task) {
        taskMapper.insertSelective(task);
        return task;
    }

    public Task get(Integer id) {
        return Optional.ofNullable(taskMapper.selectByPrimaryKey(id))
                .orElseThrow(() -> new BusinessException(-3, "未查询到该任务"));
    }

    public PageData<Task> select(TaskQuery query) {
        PageData<Task> pageData = new PageData<>();
        TaskExample example = new TaskExample();
        TaskExample.Criteria criteria = example.createCriteria();
        FunctionUtil.whenNonNullDo(criteria::andIdIn, query.ids);
        FunctionUtil.whenNonNullDo(criteria::andUidEqualTo, query.uid);
        FunctionUtil.whenNonNullDo(criteria::andStatusEqualTo, query.status);
        FunctionUtil.whenNonNullDo(criteria::andTypeEqualTo, query.type);
        FunctionUtil.whenNonNullDo(criteria::andPlatformEqualTo, query.platform);
        pageData.count = taskMapper.countByExample(example);
        example.setLimit(query.getLimit());
        example.setOffset(query.getOffset());
        pageData.list = taskMapper.selectByExample(example);
        return pageData;
    }

    public Task update(Task task) {
        task.setCreatetime(null);
        task.setUpdatetime(null);
        task.setUid(null);
        taskMapper.updateByPrimaryKeySelective(task);
        return task;
    }

    public void delete(Integer id) {
        taskMapper.deleteByPrimaryKey(id);
    }


    public void feedback(TaskFeedback taskFeedback) {
        Optional.ofNullable(taskFeedback).orElseThrow(() -> new BusinessException(-2, "参数错误"))
                .feedbacks.forEach(a -> ExecutorPool.getInstance().execute(() -> feedback(a)));
    }

    public void feedback(Feedback feedback) {
        if (NumberUtil.parseInt(feedback.lowPrice) <= 0) {
            return;
        }
        Commodity commodity = new Commodity();
        int taskId = Optional.ofNullable(feedback.taskId).orElse(0);
        int lowPrice = NumberUtil.parseInt(feedback.lowPrice);
        int price = NumberUtil.parseInt(feedback.price);
        int originalPrice = NumberUtil.parseInt(feedback.originalPrice) == 0 ? price :
                NumberUtil.parseInt(feedback.originalPrice);
        commodity.setTaskId(taskId);
        commodity.setName(feedback.name);
        commodity.setLowPrice(lowPrice);
        commodity.setPrice(price);
        commodity.setProductId(feedback.productId);
        commodity.setUrl(feedback.url);
        commodity.setSendPrice((int) NumberUtil.mul(lowPrice, 0.8));
        commodity.setOriginalPrice(originalPrice);
        commodity.setPercent((float) NumberUtil.div((float) price, (float) originalPrice, 2));
        CommodityExample example = new CommodityExample();
        example.createCriteria().andProductIdEqualTo(feedback.productId).andTaskIdEqualTo(taskId);
        List<Commodity> list = Optional.ofNullable(commodityMapper.selectByExample(example)).orElseGet(ArrayList::new);
        int count = list.size();
        int commodityId;
        if (count == 0) {
            commodityMapper.insertSelective(commodity);
            commodityId = commodity.getId();
        } else {
            commodityId = list.get(0).getId();
            // 1.当价格变动，更新
            // 2.当原来的最低价大于现在的最低价，并发送邮件通知
            if (!list.get(0).getPrice().equals(commodity.getPrice())) {
                commodity.setId(commodityId);
                commodity.setLowPrice(null);
                commodity.setName(null);
                commodity.setProductId(null);
                commodity.setUrl(null);
                commodity.setTaskId(null);
                commodity.setSendPrice(null);
                commodity.setOriginalPrice(null);
                commodity.setPercent((float) NumberUtil.div((float) price, (float) list.get(0).getOriginalPrice(), 2));
                if (list.get(0).getLowPrice() > lowPrice) {
                    commodity.setLowPrice(lowPrice);
                    commodity.setLowtime(new Date());
                    if (list.get(0).getSendPrice() >= lowPrice) {
                        commodity.setSendPrice(lowPrice);
                        String subject = StrUtil.format("监控反馈");
                        String content = mailService.getEmailContent(feedback.name, lowPrice, feedback.url,
                                commodityId, list.get(0).getPlatform(), (list.get(0).getOriginalPrice() - price));
                        ExecutorPool.getInstance().execute(() -> mailService.send(subject, content, taskId, true));
                    }
                }
                commodityMapper.updateByPrimaryKeySelective(commodity);
            }
        }
        CommodityPrice commodityPrice = new CommodityPrice();
        commodityPrice.setCommodityId(commodityId);
        commodityPrice.setPrice(lowPrice);
        commodityPriceMapper.insertSelective(commodityPrice);
    }

    private CommodityPrice convert(FeedbackPrice p, Integer commodityId) {
        CommodityPrice commodityPrice = new CommodityPrice();
        commodityPrice.setCommodityId(commodityId);
        commodityPrice.setPrice(NumberUtil.parseInt(p.price));
        return commodityPrice;
    }


}
