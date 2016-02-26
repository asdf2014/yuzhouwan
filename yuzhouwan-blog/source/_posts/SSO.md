title: SSO
date: 2014-11-16 17:28:19
tags:
- SSO
- Session
- Cookie

categories:
- SSO

---

##__SSO 是什么 ?__
<font size=3> &nbsp;&nbsp; *SSO <font size=2> (Single Sign-on)* , 单点登录
<font size=3> &nbsp; 一个多系统共存的环境下，用户在一处登录后，就不用在其他系统中登录，也就是用户的一次登录能得到其他所有系统的信任 - - [cutesource][1]
<font size=3> &nbsp; 在多个应用系统中，用户只需要登录一次就可以访问所有相互信任的应用系统。它包括可以将这次主要的登录映射到其他应用中用于同一个用户的登录的机制 - - [百度百科][2]

##__为什么要有 SSO ?__
<font size=3> &nbsp; 尤其，大型网站背后是成百上千的子系统，用户一次操作或交易可能涉及到几十个子系统的协作。
<font size=3> &nbsp; 如果每次子系统都需要用户认证，不仅用户会疯掉，各子系统也会为这种重复认证的逻辑搞疯掉。


##__SSO 框架__

### &nbsp; *__CAS__ <font size=2> (Central Authentication Server)*
<font size=3> &nbsp;&nbsp;&nbsp;&nbsp; 是 Yale 大学发起的一个开源项目，据统计，大概每 10 个采用开源构建 Web SSO 的 Java 项目，就有 8 个使用 CAS


### &nbsp; __*Passport 认证服务*__
<font size=3> &nbsp;&nbsp;&nbsp;&nbsp; ASP.NET 基于 Form 的认证方法


[1]:http://blog.csdn.net/cutesource/article/details/5838693
[2]:http://baike.baidu.com/subview/190743/15011802.htm?fr=aladdin
