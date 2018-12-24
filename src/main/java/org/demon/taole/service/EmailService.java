package org.demon.taole.service;

import org.demon.bean.PageData;
import org.demon.taole.bean.EmailQuery;
import org.demon.taole.mapper.EmailMapper;
import org.demon.taole.pojo.Email;
import org.demon.taole.pojo.EmailExample;
import org.demon.util.FunctionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private EmailMapper emailMapper;

    public PageData<Email> select(EmailQuery query) {
        EmailExample example = new EmailExample();
        EmailExample.Criteria criteria = example.createCriteria();
        FunctionUtil.whenNonNullDo(criteria::andToAddressEqualTo, query.to);
        PageData<Email> pageData = new PageData<>();
        pageData.count = emailMapper.countByExample(example);
        example.setLimit(query.getLimit());
        example.setOffset(query.getOffset());
        example.setOrderByClause(" createtime desc ");
        pageData.list = emailMapper.selectByExample(example);
        return pageData;
    }
}
