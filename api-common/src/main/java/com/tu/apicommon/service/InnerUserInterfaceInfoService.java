package com.tu.apicommon.service;

import com.tu.apicommon.model.entity.UserInterfaceInfo;

/**
* @author The tu
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2024-01-30 23:11:10
*/
public interface InnerUserInterfaceInfoService {

    /**
     * 调用接口次数+1,剩余次数-1
     */
    boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 查询用户是否还有调用次数
     */
    UserInterfaceInfo getByInterfaceInfoId(long interfaceInfoId);

}
