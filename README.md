基于 DolphinScheduler 3.0.0 开发

文档地址：http://IP:12345/dolphinscheduler/doc.html

本地开发
1. 本地需安装 zookeeper 并启动
2. 拉取代码并且编译： mvn clean package -am  -Dmaven.test.skip=true 
3. 数据库的连接配置需改动三处 dolphinscheduler-master 和 dolphinscheduler-worker 以及 dolphinscheduler-api
4. 启动 MasterServer WorkerServer ApiApplicationServer 即可

编译部署
1. 跳过测试，格式检测：mvn clean install -Prelease '-Dmaven.test.skip=true' '-Dcheckstyle.skip=true' '-Dmaven.javadoc.skip=true'
2. 文件在 dolphinscheduler-dist/target/apache-dolphinscheduler-3.0.1-SNAPSHOT-bin.tar.gz
3. 启动 sh ./bin/install.sh
4. 配置文件修改位置：scrip/env/dolphinscheduler_env.sh

#配置资源中心（采用S3存储）
1. 修改api-server配置文件信息，进入<service>/conf/common.properties
resource.storage.type=S3
aws.access.key.id=<your-access-key-id>
aws.secret.access.key=<your-access-key-secret>
aws.region=cn-north-1
aws.endpoint=<your-endpoint-address>
2. 对象存储Minio创建bucket: dolphinscheduler-test
3. 重启api-server
bash ./bin/dolphinscheduler-daemon.sh stop api-server
bash ./bin/dolphinscheduler-daemon.sh start api-server
4. 二次开发TODO
org.apache.dolphinscheduler.common.utils.S3Utils中BUCKET_NAME值，改从<service>/conf/common.properties中读取。
同时common.properties中添加aws.s3.bucket.name=<your-bucket-name>
