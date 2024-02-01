package com.yupi.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.tu.apicommon.model.entity.UserInterfaceInfo;

/**
* @author The tu
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2024-01-30 23:11:10
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 调用接口次数+1,剩余次数-1
     */
    boolean invokeCount(long interfaceInfoId, long userId);

}
