package com.yupi.project.service.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tu.apicommon.common.ErrorCode;
import com.tu.apicommon.exception.BusinessException;
import com.tu.apicommon.model.entity.UserInterfaceInfo;
import com.tu.apicommon.service.InnerUserInterfaceInfoService;
import com.yupi.project.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }

    @Override
    public UserInterfaceInfo getByInterfaceInfoId(long interfaceInfoId) {
        if (interfaceInfoId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interfaceInfoId", interfaceInfoId);
        return userInterfaceInfoService.getOne(queryWrapper);
    }
}
