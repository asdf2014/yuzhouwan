title: Hadoop RPC 源码领略
date: 2014-11-16 18:03:04
tags:
- Hadoop
- RPC
categories:
- Hadoop
- RPC

---

##__什么是 RPC ?__
&nbsp;&nbsp; <font size=3> RPC（Remote Procedure Call Protocol）——远程过程调用协议，它是一种通过网络从远程计算机程序上请求服务，而不需要了解底层网络技术的协议。 - - [百度百科][23]
&nbsp;&nbsp; <font size=3> 远程过程调用（Remote Procedure Call）是一种常用的分布式网络通讯协议，它允许运行于一台计算机的程序调用另一台计算机的子程序，同时将网络的通信细节隐藏起来，使得用户无需额外地外这个交互作用编程。 - - [董的博客][24]

##__为什么要有 RPC ?__
###**地域性**：
&nbsp;&nbsp; <font size=3> 当主机不可达时，通过远程调用可以使得终端操作目标机器成为可能。
###**含糖性**：
&nbsp;&nbsp; <font size=3> 底层的网络通信细节封装入 API，方便网络分布式系统的开发。
###**模块化**：
&nbsp;&nbsp; <font size=3> 在 Hadoop 分布式系统中，上层的分布式子系统（MapReduce、YARN、HDFS...）能够共用这个网络通信模块。

##__RPC 工作原理__
1. ##__ <font color='blue'> *启动 Hadoop 集群的 DFS、YARN*__
![hdfs][1]
2. ##__ <font color='blue'> *可以使用 Explorer 的 50070 端口，查看 NameNode*__
![host - yuzhouwan][2]
3. ##__ <font color='blue'> *通过 FileSystem 的 get(uri, conf, user) 初始化 FS*__
![FileSystem][3]
4. ##__ <font color='blue'> *抽象类 FileSystem 拥有 13 个子类*__
![FileSystems][4]
5. ##__ <font color='blue'> *加载配置文件，并给相应属性赋值*__
![FileSystem.get(uri,conf,user)][5]
6. ##__ <font color='blue'> *反射 org.apache.hadoop.hdfs.DistributedFileSystem*__
![ReflectionUtils.newInstance--DFS][6]
7. ##__ <font color='blue'> *调用 initialize(uri, conf) 初始化 DFS*__
![DFS.initialize][7]
8. ##__ <font color='blue'> *获得 DFSClient 代理*__
![DFSClient(nameNodeUri,rpcNamenode,conf,stats)][8]
9. ##__ <font color='blue'> *创建代理*__
![Proxy][9]
10. ##__ <font color='blue'> *让 DFSClient 持有 uri、conf、statistics*__
![DFSClient][10]
11. ##__ <font color='blue'> *解析 uri*__
![URI.create][11]
12. ##__ <font color='blue'> *根据用户组信息中的简单用户名，获取工作路径*__
![DFS.getHomeDirectory][12]
13. ##__ <font color='blue'> *FileSystem 完成初始化*__
![DFS-is-ok!!!][13]
14. ##__ <font color='blue'> *利用 FS.open(path) 打开读取流*__
![DistributedFileSystem][14]
15. ##__ <font color='blue'> *解析 path*__
![Path][15]
16. ##__ <font color='blue'> *装饰 open() 方法*__
![FSDataInputStream--open(path)][16]
17. ##__ <font color='blue'> *FileSystemLinkResolver 回调函数*__
![DFS.open(f,bufferSize)][17]
18. ##__ <font color='blue'> *HdfsDataInputStream 读取流*__
![HdfsDataInputStream][18]
19. ##__ <font color='blue'> *FileOutputStream 写入流*__
![FileOutputStream][19]
20. ##__ <font color='blue'> *缓冲池*__
![buffSize][20]
21. ##__ <font color='blue'> *IOUtils 封装的拷贝方法*__
![IOUtiles.copyBytes(in,out,buffSize)][21]
22. ##__ <font color='blue'> *Download over!*__
![RPC-success][22]


[1]:/../2014-11-16/hdfs.png
[2]:/../2014-11-16/host-yuzhouwan.png
[3]:/../2014-11-16/FileSystem.png
[4]:/../2014-11-16/FileSystems.png
[5]:/../2014-11-16/FileSystem.get(uri,conf,user).png
[6]:/../2014-11-16/ReflectionUtils.newInstance--DFS.png
[7]:/../2014-11-16/DFS.initialize.png
[8]:/../2014-11-16/DFSClient(nameNodeUri,rpcNamenode,conf,stats).png
[9]:/../2014-11-16/Proxy.png
[10]:/../2014-11-16/DFSClient.png
[11]:/../2014-11-16/URI.create.png
[12]:/../2014-11-16/DFS.getHomeDirectory.png
[13]:/../2014-11-16/DFS-is-ok!!!.png
[14]:/../2014-11-16/DistributedFileSystem.png
[15]:/../2014-11-16/Path.png
[16]:/../2014-11-16/FSDataInputStream--open(path).png
[17]:/../2014-11-16/DFS.open(f,bufferSize).png
[18]:/../2014-11-16/HdfsDataInputStream.png
[19]:/../2014-11-16/FileOutputStream.png
[20]:/../2014-11-16/buffSize.png
[21]:/../2014-11-16/IOUtiles.copyBytes(in,out,buffSize).png
[22]:/../2014-11-16/RPC-success.png

[23]:http://baike.baidu.com/item/%E8%BF%9C%E7%A8%8B%E8%BF%87%E7%A8%8B%E8%B0%83%E7%94%A8%E5%8D%8F%E8%AE%AE?fromtitle=RPC&fr=aladdin
[24]:http://dongxicheng.org/
