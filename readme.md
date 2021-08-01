# JAVA blog项目

1. 项目选型（目前主要技术栈,持续更新ing）
    * SpringBoot
    * Spring Security
    * thymeleaf
    * Alibaba FastJson
    * Mybatis Plus
    * Mysql
   * Hikari
   * JWT(2021-07-31 考虑添加)
   > 本段采用thymeleaf模板引擎开发，采用服务器渲染的方法，减少用户等待时长，优化用户体验，有效的减少前端渲染时长。
2. 开发日志
   + 2021-07-29

     > 初始化框架，明确架构

    + 2021-07-30

      > 1.使用 Spring security框架
      >
      > 2.使用 mybatis plus框架
      >
      > 3.使用BCryptPasswordEncoder加密密码

   - 2021-07-31
   
     > 1. 数据库层实现如下图所示的表结构
     > 2. 优化数据库结构
     > 3. spring security的代码优化
     > 4. 考虑使用JWT技术栈来实现基于token的认证

   
   
   ![image-20210731230500586](https://i.loli.net/2021/07/31/Ube8LM6CqHrTXz7.png)
   
   - 2021-08-01
   
   > 1. 明确数据库架构（下图）
   >
   >    !(https://files.catbox.moe/skbjou.png)![image-20210801195214504](https://i.loli.net/2021/08/01/j1DgOFfAtRLWQpV.png)
   >
   > 2. 修改entity实体类，完成entity-table映射关系
   >
   > 3. 完善Spring Security权限控制（目前权限暂从数据库中存储，后期实现通过reidis存储加快获取速度）
   >
   > 4. 完善代码注释
   
   

