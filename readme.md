#  open-sso 单点登录

## 一、常见sso方案

1.基于传统cookie和seesion
seesion复制共享的弊端决定这个方案并不适合大型互联网应用

2.基于无状态token
无状态有无状态的优点，但是目前笔者所知的大型互联网公司基本都没有使用这个方案，因为无状态很难解决修改密码或token被伪造后主动失效等问题

3.cookie和token结合有状态sso
首先cookie是一个成熟的方案，有人说app不适合使用cookie其实并不恰当，安卓ios都有封装cookie的组件，仿cookie也可以算cookie原理是一样的；
笔者工作的公司有4亿左右用户，使用的就是基于cookie的有状态协议，服务端也会存储认证cookie信息，结合加密和签名以及父级域名cookie实现多个子系统统一登录态


## 二、框架特点

- 使用简单：客户端只需简单配置即可使用，提供使用demo
- 轻量级：公用包依赖少，系统依赖少(只依赖redis和公司的rpc框架)
- 安全：客户凭证加密(AES加密)、签名、重定向劫持防护
- 防伪造和恶意流量：伪造凭证直接拦截在客户端，流量不会到达认证中心（随意修改和伪造cookie在客户端层面就会被拦截）
- 可区分不同类型客户端：按照约定方式可区分来源app、pc、h5、wq等
- 分布式：客户端与认证中心均可分布式部署，提高系统可用性
- 单点登录：只需要登录一次就可以访问所有同顶级域名的一级和二级系统
- 跨域：支持跨域

## 三、dome快速入门

1、示例代码如sso-clieng-demo，您只需要引入jar包，并添加一个拦截器com.hk.sso.client.interceptor.mvc.MvcLoginInterceptor即可开始使用
`

        <dependency>
            <groupId>com.hk</groupId>
            <artifactId>sso-client</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
`


------------

2、配置
如sso-clieng-demo中application.properties文件，
详细的配置属性参见com.hk.sso.client.interceptor.LoginInterceptor源码注释

server.port=8081


客户端cookie名称
sso.pcCookieName=pt_key
编码
sso.charset=UTF-8

是否需要重定向到sso登录页
sso.needRedirect=true
sso登录页面地址
sso.ssoLoginUrl=http://ssoindex.heroking.com
登陆成功回调地址
sso.redirectUrl=http://ssoclient.heroking.com:8081/test

是否启用cookie签名验证
sso.cookieSignValidate=true
cookie签名验证类型，1：客户端验证；2：服务端验证
sso.cookieSignType=1

cookie加密key
sso.cookieEncryptKey=ac27c9101b3f22d0224f8d9d36ba5be7

cookie签名客户端验证的加密key
sso.cookieSignKey=8f8c36c8071fc4d15e49888902f348c8

3、dome工程准备

配置本地host：
127.0.0.1 ssoclient.heroking.com
127.0.0.1 ssoserver.heroking.com
127.0.0.1 ssoindex.heroking.com

启动本地redis服务于zk服务（默认端口）
dome工程使用了redis存储登录态数据，因此需要本地启动一个redis服务，远程认证中心使用了dubbo依赖zookeeper需要本地启动一个zk
真实生产环境每个公司使用的rpc框架有所不同，可以自己实现com.hk.sso.common.service.SsoRemoteService接口进行扩展
各登录态说明见：com.hk.sso.common.enums.LoginStatus

为了尽可能模拟真实生产环境，登录页使用的前都断分离，vue框架
下载open-sso登录页代码：[https://github.com/herokangvip/sso-index](https://github.com/herokangvip/sso-index "https://github.com/herokangvip/sso-index")
npm run dev启动

![登录页](http://a4.qpic.cn/psb?/V10T9jnP0surO2/Ni5e7RWxKne2vBzZGU1vyNWfY3ZQfd4uX6FxbykE*ls!/m/dL8AAAAAAAAAnull&bo=gQFRAQAAAAADB*I!&rf=photolist&t=5 "登录页")

4、dome工程演示

分别启动sso-client-demo、sso-server-demo
访问：[http://ssoclient.heroking.com:8081/index](http://ssoclient.heroking.com:8081/index "http://ssoclient.heroking.com:8081/index")
由于未登录跳转到登录页，输入用户名admin，密码admin，点击登录跳转到之前配置的回调地址
sso.redirectUrl=http://ssoclient.heroking.com:8081/test
查看登录态cookie
![登录态cookie](http://m.qpic.cn/psb?/V10T9jnP0surO2/ido1*TY1zenhzd3keiVUpbAlSTgv4WRa1io5ESJD8Mk!/b/dIQAAAAAAAAA&bo=KALhAQAAAAADB.g!&rf=viewer_4 "登录态cookie")

## 四、备忘
使用https协议，重要操作做攻击防护（xss、crsf等常见漏洞）和二次验证（短信密保等）
前后端要做防重复点击与防重复请求
js做混淆加密处理
登录操作限制单位时间内请求次数或者对超过次数的用户展示校验码滑块等
认证中心建议使用成熟的rpc框架不要使用http，rpc自带的监控权限控制限流等功能很有必要
本框架不包含passport部分，风控、登录注册验证等逻辑


