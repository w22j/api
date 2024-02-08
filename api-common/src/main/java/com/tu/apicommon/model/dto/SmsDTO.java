package com.tu.apicommon.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsDTO implements Serializable {

    private static final long serialVersionUID = 8504215015474691352L;
    String phoneNum;

    String code;
}