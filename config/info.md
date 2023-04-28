#### 镜像版本
``` text
nacos/nacos-server                               v2.2.1              faff56ad2ef5   5 weeks ago      1.17GB
redis                                            6.2.8               7c133222c17b   3 months ago     113MB
docker.elastic.co/elasticsearch/elasticsearch    8.5.2               b19437f14a3b   5 months ago     1.29GB
docker.elastic.co/kibana/kibana                  8.5.2               8bec3366fd9a   5 months ago     707MB
nginx                                            1.22.0              08a1cbf9c69e   6 months ago     142MB
xuxueli/xxl-job-admin                            2.3.1               252b168021da   11 months ago    237MB
openjdk                                          17.0.2-jdk-buster   629cbc2df164   12 months ago    654MB
openjdk                                          17.0.2-jdk-slim     37cb44321d04   12 months ago    408MB
minio/minio                                      latest              e31e0721a96b   15 months ago    406MB
registry                                         2                   b8604a3fe854   17 months ago    26.2MB
rabbitmq                                         3.9.8-management    316e073199ea   18 months ago    253MB
mysql                                            8.0.26              9da615fced53   18 months ago    514MB
```
#### 执行语句
```text
docker run --name checkcode -p 1020:1020 --add-host centos006:192.168.164.133 -d 192.168.164.133:1026/xcplus/xcplus-checkcode:0.0.1-SNAPSHOT 

docker run --name gateway -p 1006:1006 --add-host centos006:192.168.164.133 -d 192.168.164.133:1026/xcplus/xcplus-gateway:0.0.1-SNAPSHOT 

docker run --name auth -p 1019:1019 --add-host centos006:192.168.164.133 \
 --add-host www.51xuecheng.cn:192.168.164.133  -d 192.168.164.133:1026/xcplus/xcplus-auth:0.0.1-SNAPSHOT 
 
docker run --name content-api -p 1002:1002 --add-host centos006:192.168.164.133 \
 -d 192.168.164.133:1026/xcplus/xcplus-content-api:0.0.1-SNAPSHOT 
```