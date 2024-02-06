package com.yupi.project.service.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tu.apicommon.common.ErrorCode;
import com.tu.apicommon.exception.BusinessException;
import com.tu.apicommon.model.entity.User;
import com.tu.apicommon.service.InnerUserService;
import com.yupi.project.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getInvokeUser(String accessKey) {
        if (StringUtils.isBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey", accessKey);
        return userMapper.selectOne(queryWrapper);
    }
}
