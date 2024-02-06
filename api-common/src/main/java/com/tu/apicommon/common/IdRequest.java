package com.tu.apicommon.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 发布下线的接口请求
 *
 * 
 */
@Data
public class IdRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}