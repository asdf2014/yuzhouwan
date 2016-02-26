title: DIRT
date: 2014-11-03 14:15:02
tags: 
- Node.js
categories: 
- Node.js
---
###DIRT:数据密集型实时(data-intensive real-time)
__为什么适用于 Node 开发 ? __

<font size=4>&nbsp;&nbsp;因为 Node 自身在 I/O 上非常轻量，它善于将数据从一个管道混排 或 代理到另一个管道上，这能在处理大量请求时持有很多开放的连接，并且只占用一小部分内存。(如同浏览器一样，保证了响应能力)

__Web 发展形势__
<font size=4>&nbsp;&nbsp;不管是用实时组件增强已有程序，还是打造全新的程序，Web 都在朝着响应性和协作型环境逐渐进发。
<font size=4>&nbsp;&nbsp;而这种新型的 Web 应用程序需要一个能够实时相应大量并发请求的平台来支撑它们。(除此之外，还有 I/O 负载较重的程序也可以用到)

__Node 作为 JavaScript 程序的平台__
- Timer API (for example, setTimeout)
- Console API (for example, console.log)
- Network and File I/O modules (HTTP, TLS, HTTPS, filesystem (POSIX), Datagram (UDP), and NET (TCP))