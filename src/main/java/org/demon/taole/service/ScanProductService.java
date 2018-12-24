package org.demon.taole.service;

import cn.hutool.core.util.StrUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.demon.exception.BusinessException;
import org.demon.taole.mapper.ScanProductMapper;
import org.demon.taole.pojo.ScanProduct;
import org.demon.taole.pojo.ScanProductExample;
import org.demon.taole.pojo.ScanProductPrice;
import org.demon.util.FunctionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-12 10:52
 */
@Service
@CommonsLog
public class ScanProductService {

    @Autowired
    private ScanProductMapper scanProductMapper;
    @Autowired
    private ScanProductPriceService scanProductPriceService;
    @Autowired
    private MailService mailService;


    /**
     * 保存jd数据
     *
     * @param pojo {@link ScanProduct}
     * @return {@link ScanProduct}
     */
    public ScanProduct save(ScanProduct pojo) {
        ScanProductExample example = new ScanProductExample();
        example.createCriteria().andProductIdEqualTo(pojo.getProductId());
        List<ScanProduct> list =
                Optional.ofNullable(scanProductMapper.selectByExample(example)).orElseGet(ArrayList::new);
        int count = list.size();
        if (count == 0) {
            FunctionUtil.check(scanProductMapper.insertSelective(pojo) <= 0,
                    new BusinessException(-2, "scanProduct save error"));
            scanProductPriceService.save(convert(pojo));
            return pojo;
        }
        ScanProduct scanProduct = list.get(0);
        if (!scanProduct.getPrice().equals(pojo.getPrice())) {
            scanProduct.setPrice(pojo.getPrice());
            scanProductMapper.updateByPrimaryKeySelective(scanProduct);
            sendMail(scanProduct);
        }
        scanProduct.setPrice(pojo.getPrice());
        scanProductPriceService.save(convert(scanProduct));
        return pojo;
    }

    private ScanProductPrice convert(ScanProduct scanProduct) {
        ScanProductPrice price = new ScanProductPrice();
        price.setPrice(scanProduct.getPrice());
        price.setScanProductId(scanProduct.getId());
        return price;
    }

    private void sendMail(ScanProduct scanProduct) {
        log.info(StrUtil.format("\n监控反馈:\n{}\n{}", scanProduct.getName(), scanProduct.getPrice()));
        String subject = StrUtil.format("监控反馈");
        String content = StrUtil.format("{}", scanProduct.getUrl());
        mailService.send(subject, content, 1, true);
    }

    public ScanProduct get() {
        return scanProductMapper.selectByPrimaryKey(1L);
    }

}

