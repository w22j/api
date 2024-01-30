package com.yupi.project.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @TableName product
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 请求参数
     */
    private String userRequestParams;

}