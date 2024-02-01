package com.yupi.project.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tu.apicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author The tu
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2024-01-30 23:11:10
* @Entity generator.domain.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    /**
     * 获取调用次数排名靠前的接口
     * @param limit
     * @return
     */
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




