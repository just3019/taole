package org.demon.taole.service;

import cn.hutool.core.util.NumberUtil;
import org.demon.bean.PageData;
import org.demon.exception.BusinessException;
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
import org.demon.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-18 11:55
 */
@Service
public class TaskService {

    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private CommodityMapper commodityMapper;
    @Autowired
    private CommodityPriceMapper commodityPriceMapper;


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


    @Transactional
    public void feedback(TaskFeedback taskFeedback) {
        Optional.ofNullable(taskFeedback).orElseThrow(() -> new BusinessException(-2, "参数错误"))
                .feedbacks.forEach(this::feedback);
    }

    @Transactional
    public void feedback(Feedback feedback) {
        Commodity commodity = new Commodity();
        int taskId = Optional.ofNullable(feedback.taskId).orElse(0);
        commodity.setTaskId(taskId);
        commodity.setName(feedback.name);
        commodity.setLowprice(NumberUtil.parseInt(feedback.lowPrice));
        commodity.setPrice(NumberUtil.parseInt(feedback.price));
        commodity.setProductId(feedback.productId);
        commodity.setUrl(feedback.url);
        CommodityExample example = new CommodityExample();
        example.createCriteria().andProductIdEqualTo(feedback.productId).andTaskIdEqualTo(taskId);
        List<Commodity> list = Optional.ofNullable(commodityMapper.selectByExample(example)).orElseGet(ArrayList::new);
        int count = list.size();
        if (count == 0) {
            commodityMapper.insertSelective(commodity);
        } else {
            commodity.setId(list.get(0).getId());
            if (list.get(0).getLowprice() <= commodity.getLowprice()) {
                commodity.setLowprice(null);
            }
            commodityMapper.updateByPrimaryKeySelective(commodity);
        }
        List<CommodityPrice> commodityPrices = feedback.feedbackPrices.stream().map(a -> convert(a,
                commodity.getId())).collect(Collectors.toList());
        if (StringUtil.isNotEmpty(commodityPrices)) {
            commodityPriceMapper.insertByBatch(commodityPrices);
        }
    }

    private CommodityPrice convert(FeedbackPrice p, Integer commodityId) {
        CommodityPrice commodityPrice = new CommodityPrice();
        commodityPrice.setCommodityId(commodityId);
        commodityPrice.setPrice(NumberUtil.parseInt(p.price));
        return commodityPrice;
    }


}
