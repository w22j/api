package com.tu.apicommon.service;

import com.tu.apicommon.model.entity.InterfaceInfo;

/**
* @author The tu
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-01-29 21:51:17
*/
public interface InnerInterfaceInfoService {

    /**
     * 查询模拟接口是否存在（请求路径，请求方法）
     */
    InterfaceInfo getInterfaceInfo(String url, String method);

}
