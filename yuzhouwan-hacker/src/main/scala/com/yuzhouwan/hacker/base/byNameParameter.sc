var assertionsEnabled = true

def myAssert(predicate: () => Boolean): Unit =
  if (assertionsEnabled && !predicate())
    throw new AssertionError

myAssert(() => 5 > 3)
//myAssert(5 > 3)   //error

myAssert(() => 1 / 0 == 0)

def byNameAssert(predicate: => Boolean): Unit =
  if (assertionsEnabled && !predicate)
    throw new AssertionError

byNameAssert(5 > 3)
// 传名参数 的意义是，可以传入一个函数，而非函数计算后的结果
byNameAssert(1 / 0 == 0)
