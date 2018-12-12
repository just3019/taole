package org.demon.taole.service;

import org.demon.taole.mapper.ScanProductPriceMapper;
import org.demon.taole.pojo.ScanProductPrice;
import org.demon.taole.pojo.ScanProductPriceExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-12 10:53
 */
@Service
public class ScanProductPriceService {

    @Autowired
    private ScanProductPriceMapper scanProductPriceMapper;

    /**
     * 保存
     *
     * @param pojo {@link ScanProductPrice}
     * @return {@link ScanProductPrice}
     */
    public ScanProductPrice save(ScanProductPrice pojo) {
        scanProductPriceMapper.insertSelective(pojo);
        return pojo;
    }

    public void delete(Long scanProductId) {
        ScanProductPriceExample example = new ScanProductPriceExample();
        example.createCriteria().andScanProductIdEqualTo(scanProductId);
        scanProductPriceMapper.deleteByExample(example);
    }
}
