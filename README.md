为解决目前市场上的停车场缺乏互联网化，停车难的问题而开发的服务系统。
该系统使用技术有：jsp、servlet、javabean、html、css、js、jquery、ajax、bootstrsp，jdbc；使用Mysql数据库。···
系统按照MVC模式实现，View使用jsp+js来完成，view主要通过ajax和Controller交互，Controller使用servlet来完成，Model使用javabean和对应的DAO层来完成。

用户端主要功能有：注册登录，查看个人信息(账户余额等)，查看个人车牌信息，添加车牌证件，在线查看停车场车位状态(空闲，已被预定，正在使用)，在线预定车位
管理员端主要功能：查看停车场车位信息，管理预定申请，管理车辆进入和离开，自动计费和扣费，查看用户信息，用户充值，统计收入