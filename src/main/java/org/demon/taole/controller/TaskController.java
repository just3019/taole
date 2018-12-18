package org.demon.taole.controller;

import org.demon.bean.PageData;
import org.demon.exception.BusinessException;
import org.demon.taole.bean.TaskFeedback;
import org.demon.taole.bean.TaskQuery;
import org.demon.taole.pojo.Task;
import org.demon.taole.service.TaskService;
import org.demon.util.FunctionUtil;
import org.demon.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-18 11:48
 */
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping(value = "/task")
    public Task save(@RequestBody Task task) {
        return taskService.save(task);
    }

    @GetMapping(value = "/task/{id}")
    public Task get(@PathVariable("id") Integer id) {
        return taskService.get(id);
    }

    @PostMapping(value = "/tasks")
    public PageData<Task> select(@RequestBody TaskQuery query) {
        return taskService.select(query);
    }

    @PutMapping(value = "/task")
    public Task update(@RequestBody Task task) {
        FunctionUtil.check(StringUtil.isNull(task.getId()), new BusinessException(-2, "参数错误"));
        return taskService.update(task);
    }

    @DeleteMapping(value = "/task/{id}")
    public void delete(@PathVariable("id") Integer id){
        taskService.delete(id);
    }

    
    @PostMapping(value = "/task/feedback")
    public void feedback(@RequestBody TaskFeedback taskFeedback) {
        taskService.feedback(taskFeedback);
    }

}
