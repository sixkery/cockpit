基于 DolphinScheduler 3.0.0 开发

文档地址：http://IP:12345/dolphinscheduler/doc.html

本地开发
1. 本地需安装 zookeeper 并启动
2. 拉取代码并且编译： mvn clean package -am  -Dmaven.test.skip=true 
3. 数据库的连接配置需改动三处 dolphinscheduler-master 和 dolphinscheduler-worker 以及 dolphinscheduler-api
4. 启动 MasterServer WorkerServer ApiApplicationServer 即可