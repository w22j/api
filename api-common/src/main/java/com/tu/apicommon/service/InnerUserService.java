package com.tu.apicommon.service;



import com.tu.apicommon.model.entity.User;

/**
 * 用户服务
 *
 * 
 */
public interface InnerUserService {
    /**
     * 查询是否已分配给用户密钥
     */
    User getInvokeUser(String accessKey);
}
