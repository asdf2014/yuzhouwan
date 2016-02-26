title: Node 模块
date: 2014-11-05 09:01:15
tags:
- Node.js
- Module
categories:
- Node.js
- Module
---
##__为什么要有 Node 模块 ?__
<font size=3>模块，是 Node 让代码易于重用的一种组织和包装方式

##__创建模块__
```node
var a = 'a';
function A() {
    console.log(a);
}
exports.printA = A;
```

##__引入模块__
```node
var a = require('./module_');
a.printA();
```

##__模块暴露构造函数__
```node
//定义
var B = function (input) {
    this.input = input;
}
B.prototype.printB = function () {
    console.log(this.input);
}
module.exports = exports = B;

//调用
var B = require('./module_');
var b = new B('asdf');
b.printB();
```
+ <font size=4> __*exports*__
<font size=3>&nbsp;&nbsp;只是对 module.exports 的一个全局引用，最初被定义为一个可以添加属性的空对象
+ <font size=4> __*exports.printA*__
<font size=3>&nbsp;&nbsp;是 module.exports.printA 的简写
+ <font size=4> __*exports = B*__
<font size=3>&nbsp;&nbsp;将会打破 module.exports 和 exports 之间的引用关系
+ <font size=4> __*module.exports = exports*__
<font size=3>&nbsp;&nbsp;可以修复链接

##__Module 加载流程__
![module][1]

##__目录模块__
![package.json][2]

##__Monkey Patching__
<font size=3>&nbsp;&nbsp;Node 将模块作为对象缓存起来
<font size=3>&nbsp;&nbsp;第一个文件会将模块返回的数据存到程序的内存中，第二个文件就不用再去访问和计算模块的源文件了
<font size=3>&nbsp;&nbsp;并且第二次引入有机会修改缓存的数据

[1]:/../2014-11-5/steps-to-finding-a-module.png
[2]:/../2014-11-5/steps-to-finding-a-main-file.png




