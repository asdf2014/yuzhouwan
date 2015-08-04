title: Antlr
date: 2015-02-02 11:09:26
tags:
- Antlr
categories:
- Antlr

---

## __<font color='blue'>*什么是 Antlr?*__

&nbsp;&nbsp; <font size=3>ANTLR—Another Tool for Language Recognition, 其前身是 PCCTS, 它为包括 Java, C++, C# 在内的语言提供了一个通过语法描述来自动构造自定义语言的识别器 (recognizer) , 编译器 (parser) 和解释器 (translator) 的框架。 - - [百度百科][1]

&nbsp; <font size=3>_ANTLR (ANother Tool for Language Recognition) is a powerful parser generator for reading, processing, executing, or translating structured text or binary files. It's widely used to build languages, tools, and frameworks. From a grammar, ANTLR generates a parser that can build and walk parse trees._ - - [Antlr Official Site][2]

&nbsp; <font size=3>_In computer-based language recognition, ANTLR (pronounced Antler), or ANother Tool for Language Recognition, is a parser generator that uses LL(*) parsing._ - - [WIKIPEDIA][3]

## __<font color='blue'>*为什么要有 Antlr?*__
### 简易性

	- 可以通过断言 (Predicate) 解决识别冲突
	- 支持动作 (Action) 和返回值 (Return Value)
	- 它可以根据输入自动生成语法树并可视化的显示出来

### 模块化
	- 复杂情况下，需要基于语法树遍历 (walking the tree) 生成目标代码
	- Embeded action将处理代码跟语法描述混合起来，语法复杂时使语法文件臃肿
	- 语法可能经常需要修改
	- 语法的主要表达式却不会变动，将语法识别与转换、生成 (目标代码) 等处理分离

## __<font color='blue'>*Antlr 工作机制*__

###词法分析器 (Lexer)
	分析量化字符流，翻译成离散的字符组 (也就是一堆 Token), 包括关键字，标识符，符号 (symbols) 和操作符，以供语法分析器使用

###语法分析器 (Parser)
	将 Tokens 组织起来，并转换成为目标语言语法 (默认是 java) 定义所允许的序列

###树分析器 (Tree Parser)
	用于对语法分析生成的抽象语法树进行遍历，并在先序经过每个树节点的时候，进行一些定制操作


## __<font color='blue'>*Antlr 运作流程*__

###编写 .g/.g4 文件 (asdf.g4)
```antlr
grammar asdf;
say
	: 'asdf say: ' Name
	;
Name
	: [a-z]+
	;
WS
	:[ \t\r\n]+ -> skip
	;
```

###使用 antlr 词法/语法 分析
```java
java org.antlr.v4.Tool asdf.g4
------->
asdf.tokens
asdfLexer.tokens
asdfLexer.java
asdfListener.java
asdfBaseListener.java
asdfParser.java
```

###编译，可视化
```java
javac asdf*.java
java org.antlr.v4.runtime.misc.TestRig asdf say -gui -tree

input a sentence ending with ^Z:
	say hello
```
![say-hello][4]

##__<font color='blue'>*那些年一起踩过的坑 !*__

###给每次 antlr 的结果文件添加 package
	在 .g4 中使用 @header{ ... } 添加 
```antlr
@header{
	package com.yuzhouwan.antlr;
}
```

###使用 if-else 在 exit/enter() 中判别，节点与子节点之间的关系，确定是否是 operator
```antlr
say
	: 'say' Colon Name	#Colon
	;
```

```java

asdfListener 将会得到一个定位粒度为 Colon 的访问节点

/**
 * Enter a parse tree produced by the {@code Colon}
 * labeled alternative in {@link asdfParser#say}.
 * @param ctx the parse tree
 */
void enterColon(asdfParser.ColonContext ctx);
/**
 * Exit a parse tree produced by the {@code Colon}
 * labeled alternative in {@link asdfParser#say}.
 * @param ctx the parse tree
 */
void exitColon(asdfParser.ColonContext ctx);
```

[1]:http://baike.baidu.com/link?url=0M76AbufdfB3BPoqptG8FqFZ_U-U6IloEsnc_9sCA9iPNhmFXjLCvf5MULP9YXIITmnafLH4r8GMuZ4_R_D3hq
[2]:http://www.antlr.org/
[3]:http://en.wikipedia.org/wiki/ANTLR
[4]:/../2015-2-2/say-hello.png
