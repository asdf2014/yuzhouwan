title: Session
date: 2014-11-15 15:55:36
tags:
- Session
- Java
- PHP
- JSP
- Hibernate
- WebLogic
- SSO
- HTTP
categories:
- Session

---


##__Session 是什么 ?__
<font size=3>&nbsp;&nbsp;指一个终端用户与交互系统进行通信的时间间隔，通常指从注册进入系统到注销退出系统之间所经历的时间 - - [百度百科][1]<br/>
<font size=3>&nbsp;&nbsp;代表服务器与浏览器之间的一次会话过程，这个过程是连续的，也可以是时断时续的 - - [熔岩的博客][2]<br/>
<font size=3>&nbsp;&nbsp;在 web 开发语境下，含义是指一类用来在客户端与服务器之间保持状态的解决方案 - - [我来说两句][3]<br/>

+ *Java*
```java
	- javax.servlet.http.HttpSession
```
+ *PHP*
```php
	- $_session
```
+ *JSP*
```jsp
	- HttpSession
```
+ *Hibernate*
```hibernate
	- org.hibernate interface Session
```
+ *WebLogic*
```weblogic
	- Weblogic Server session
```


##__为什么要有 Session ?__
<font size=3>&nbsp;&nbsp;HTTP 本身是无状态的，这与 HTTP 协议本身的目的是相符的。<br/>
<font size=3>&nbsp;&nbsp;当客户每次访问 web 页面，服务器重新打开新的会话时，为了维护其上下文信息（记住同一个用户）。<br/>
<font size=3>&nbsp;&nbsp;由于此类种种场景，需要让 HTTP 协议成为有状态的。<br/>


##__Session 工作原理 .__
<font size=3>&nbsp;&nbsp;session 机制是一种服务器端的机制，服务器使用一种类似于散列表的结构来保存信息。

![session][4]


##__那些年一起踩过的坑 !__

###  *session 的创建*

<font size=3>&nbsp;&nbsp;&nbsp;&nbsp;session 不是在客户端访问 server 的时候就创建，而是在服务器的某个构建 session 的语句被调用时
+ *PHP*
```php
	- session_start()
```
+ *JSP*
```jsp
	- 内置对象 session
```
+ *Java*
```java
	-  HttpServletRequest.getSession(true)
```
+ *Hibernate*
```hibernate
	- new Configuration.configure("hibernate.cfg.xml").buildSessionFactory().openSesssion()
```

### *[SSO][5] (Single sign on)*

<font size=3>  按照 Servlet 规范，session 的作用域应该仅仅限于当前应用程序下，不同的应用程序之间是不能够相互访问对方的 session 的。

<font size=3>  各个应用服务器从实际效果上都遵守了这一规范，但是实现的细节却可能各有不同，因此解决跨应用程序 session 共享的方法也不尽相同。

<font size=3>  可以借助于第三方的力量，比如使用文件、数据库、JMS 或者客户端 cookie，URL 参数或者隐藏字段等手段。

<font size=3>  还有一种较为方便的做法，就是把一个应用程序的 session 放到 ServletContext 中取得前一个应用程序的引用。



[1]:http://baike.baidu.com/view/25258.htm?fr=aladdin
[2]:http://lavasoft.blog.51cto.com/62575/275589
[3]:http://www.2cto.com/kf/201206/135471.html
[4]:/../2014-11-15/session.png
[5]:http://www.yuzhouwan.com/2014/11/16/SSO/
