<a name="M9uG2"></a>
# 项目介绍
API接口调用平台，为开发者提供API接口，提高开发者开发效率。

- 普通用户：注册登录，开通接口调用权限，浏览并使用接口。
- 后台：管理员可以调用统计和可视化分析接口的调用情况以及发布接口、下线接口、新增接口。

主要功能：

1. API 接入
2. 防止攻击（安全性）
3. 不能随便调用（限制、开通）
4. 统计调用次数
5. 计费
6. 流量保护
   <a name="gIixn"></a>
# 技术选型
<a name="q22iQ"></a>
## 后端

- Spring Boot
- Spring Boot Starter（SDK 开发）
- Dubbo（RPC）
- Nacos（注册中心）
- Spring Cloud Gateway（网关、限流、日志实现）
  <a name="wlkdy"></a>
## 启动方式
<a name="dFFqb"></a>
### 后端
api-backend：端口7529，后端接口管理（上传、下线、用户登录、接口调用等）<br />后端接口文档地址：[http://localhost:7529/api/doc.html](http://localhost:7529/api/doc.html)<br />api-gateway：端口8090，网关<br />api-interface：端口8123，提供各种接口服务（可以有很多个且分布在各个服务器）<br />api-client-sdk：客户端SDK，无端口，发送请求到网关端口（8090），由网关进行转发到后端的api-interface

<a name="kJmA0"></a>
# 详细笔记文档

- 见doc目录下
