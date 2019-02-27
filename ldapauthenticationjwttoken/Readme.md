ldapauthenticationjwttoken 可以为ldap服务上的合法用户生产一个JWT令牌，这个sample中创建了一个微服务接受用户名和密码生成JWT token并返回给调用方。

**测试服务**

1. 下载代码
2. 使用命令  `spring-boot:run` 启动服务
3. 通过脚本  ` curl -d '{"username":"john", "password":"secret"}' -H "Content-Type: application/json" -X POST http://localhost:8085/api/auth/generatetoken` 测试服务

如果是用postman等测试工具，可以采用如下的方式进行测试：

```java
HTTP Method:Post
URL: http://localhost:8085/api/auth/generatetoken
Body: {"username":"john","password":"secret"}
Content-Type: application/json
```

