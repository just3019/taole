package org.demon.taole.service;

import org.demon.bean.PageData;
import org.demon.exception.BusinessException;
import org.demon.taole.bean.TaskQuery;
import org.demon.taole.mapper.TaskMapper;
import org.demon.taole.pojo.Task;
import org.demon.taole.pojo.TaskExample;
import org.demon.util.FunctionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
