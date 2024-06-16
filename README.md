# yycache
[yycache](https://github.com/XianReallyHot-ZZH/yycache)是一个基于springboot构建的缓存server。

## 简介
yycache实现了redis的协议，支持redis的部分指令，redis-cli可以正常访问，yycache相当于是基于springboot去mock了一个redis的server。

## 当前进展

* FINISHED：基于springboot构建程序运行骨架；
* FINISHED：基于netty实现网络功能；
* doing：支持redis的RESP协议；
* doing：实现五种基本数据结构的常见操作命令；
* TODO：实现对于Lua脚本的支持；
* TODO：实现redis的集群模式；
* TODO：实现redis的哨兵模式；