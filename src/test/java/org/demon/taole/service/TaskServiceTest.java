package org.demon.taole.service;

import lombok.extern.apachecommons.CommonsLog;
import org.demon.TaoleApplication;
import org.demon.taole.bean.TaskQuery;
import org.demon.taole.pojo.Task;
import org.demon.util.JSONUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-18 13:13
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TaoleApplication.class})//classes 指定启动类,加载环境
@Transactional
@CommonsLog
public class TaskServiceTest {


    @Autowired
    private TaskService taskService;

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

}
