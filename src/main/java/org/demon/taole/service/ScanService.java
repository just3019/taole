package org.demon.taole.service;

import org.demon.exception.BusinessException;
import org.demon.taole.ScanBean;
import org.demon.taole.mapper.ScanMapper;
import org.demon.taole.pojo.Scan;
import org.demon.util.FunctionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-06 17:01
 */
@Service
public class ScanService {


    @Autowired
    private ScanMapper scanMapper;


    public void save(ScanBean bean) {
        Scan scan = covert(bean);
        FunctionUtil.check(scanMapper.insert(scan) <= 0, new BusinessException(-2, "保存监控数据失败"));
    }


    private Scan covert(ScanBean bean) {
        Scan scan = new Scan();
        scan.setName(bean.name);
        scan.setPrice(bean.price);
        scan.setSource(bean.source);
        if (bean.id != null) {
            scan.setCreatetime(new Date());
        }
        return null;
    }

    public void saveBatch(List<ScanBean> beans) {
        List<Scan> scans = beans.stream().map(this::covert).collect(Collectors.toList());
        scanMapper.insertBatch(scans);
    }
}
