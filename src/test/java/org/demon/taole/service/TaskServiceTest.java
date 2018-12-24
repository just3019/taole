package org.demon.taole.service;

import cn.hutool.core.date.DateUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.demon.TaoleApplication;
import org.demon.taole.bean.TaskQuery;
import org.demon.taole.mapper.CommodityMapper;
import org.demon.taole.mapper.CommodityPriceMapper;
import org.demon.taole.pojo.Task;
import org.demon.util.JSONUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-18 13:13
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TaoleApplication.class})//classes 指定启动类,加载环境
//@Transactional
@CommonsLog
public class TaskServiceTest {


    @Autowired
    private TaskService taskService;
    @Autowired
    private CommodityPriceMapper commodityPriceMapper;
    @Autowired
    private CommodityPriceService commodityPriceService;
    @Autowired
    private CommodityMapper commodityMapper;

    @Test
    public void test() {
        Task task = new Task();
        task.setUid(1);
        task.setJson("{\"url\":\"http://baidu.com\",\"xpath\":\"//**\",\"keyword\":\"iphone\"}");
        task.setName("测试任务");
        task.setPlatform("0");
        task.setType("1");
        log.info(JSONUtil.obj2Json(save(task)));
        get(task.getId());
        select(new TaskQuery());
        task.setName("修改测试任务名称");
        log.info(JSONUtil.obj2Json(update(task)));
        delete(task.getId());
    }


    public Task save(Task task) {
        return taskService.save(task);
    }

    public void get(Integer id) {
        log.info(JSONUtil.obj2Json(taskService.get(id)));
    }

    public void select(TaskQuery query) {
        log.info(JSONUtil.obj2Json(taskService.select(query)));
    }

    public Task update(Task task) {
        return taskService.update(task);
    }

    public void delete(Integer id) {
        taskService.delete(id);
    }


    @Test
    public void testSelect() {
        Date begin = DateUtil.parse("2018-12-21 00:00:00");
        Date end = DateUtil.parse("2018-12-21 23:59:59");
        System.out.println(begin);
        System.out.println(end);
        //        CommodityExample commodityExample = new CommodityExample();
        //        commodityExample.createCriteria().andUpdatetimeGreaterThanOrEqualTo(begin);
        //        Optional.ofNullable(commodityMapper.selectByExample(commodityExample)).orElseGet(ArrayList::new)
        //                .forEach(a -> {
        //                    List<Map<String, Object>> list = commodityPriceMapper.select(a.getId(), begin, end);
        //                    list.forEach(b -> {
        //                        //1.获取这个价格和这个价格的数量
        //                        Long count = (Long) b.get("count");
        //                        Integer price = (Integer) b.get("price");
        //                        //2.获取这个价格前一天的记录，留下第一个记录
        //                        CommodityPriceExample commodityPriceExample = new CommodityPriceExample();
        //                        commodityPriceExample.createCriteria().andCommodityIdEqualTo(a.getId())
        //                                .andPriceEqualTo(price).andCreatetimeBetween(begin, end);
        //                        commodityPriceExample.setLimit(Math.toIntExact(count - 1));
        //                        commodityPriceExample.setOrderByClause(" id desc");
        //                        List<Long> commodityPriceIds = Optional.ofNullable(commodityPriceMapper
        //                                .selectByExample(commodityPriceExample)).orElseGet(ArrayList::new).stream()
        //                                .map(CommodityPrice::getId).collect(Collectors.toList());
        //                        if (commodityPriceIds.size() == 0) {
        //                            return;
        //                        }
        //                        //3.根据id列表删除
        //                        CommodityPriceExample deleteExample = new CommodityPriceExample();
        //                        deleteExample.createCriteria().andIdIn(commodityPriceIds);
        //                        commodityPriceMapper.deleteByExample(deleteExample);
        //                    });
        //                });
    }

}
