# yycache
[yycache](https://github.com/XianReallyHot-ZZH/yycache)是一个基于springboot构建的缓存server。

## 简介
yycache实现了redis的协议，支持redis的部分指令，redis-cli可以正常访问，yycache相当于是基于springboot去mock了一个redis的server。

## 当前进展

* FINISHED：基于springboot构建程序运行骨架；
* FINISHED：基于netty实现网络功能；
* FINISHED：支持redis的RESP协议；
* FINISHED：实现五种基本数据结构：
  * String
  * List
  * Set
  * ZSet
  * Hash
* FINISHED：(部分)实现常见操作命令：
  * 基础指令: Command,Info,Ping
  * String类型指令: Set,Get,Strlen,Del,Exists,Incr,Decr,Mset,Mget
  * List类型指令: Lpush,Lpop,Rpop,Rpush,Llen,Lindex,Lrange
  * Set类型指令: Sadd,Smembers,Srem,Scard,Spop,Sismember
  * Hash类型指令: Hset,Hget,Hgetall,Hlen,Hdel,Hexists,Hmget
  * ZSet类型指令: Zadd,Zcard,Zscore,Zrem,Zrank,Zcount
* TODO：实现对于Lua脚本的支持；
* TODO：实现redis的集群模式；
* TODO：实现redis的哨兵模式；