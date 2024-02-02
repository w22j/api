package com.tu.apiinterface;

import com.tu.apiclientsdk.client.ApiClient;
import com.tu.apiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ApiInterfaceApplicationTests {

    @Resource
    ApiClient apiClient;

    @Test
    void contextLoads() {
        String name = "zhangsan";
        User user = new User();
        user.setUsername("zhangsan");
        System.out.println(apiClient.getName(name));
        System.out.println(apiClient.getNameByPost(name));
        System.out.println(apiClient.getUsernameByPost(user));


    }

}
