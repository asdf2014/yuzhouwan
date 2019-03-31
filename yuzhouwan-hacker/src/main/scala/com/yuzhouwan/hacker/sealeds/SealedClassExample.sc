val sc = new SealedClass

// sealed 关键字声明的类，其派生的子类，都必须要要在 同一源文件中
// 常见的例子是 Option 派生的 Some 和 None
sealed trait SealedTrait

class SealedClass extends SealedTrait {
  def m(): Unit = {
    println("m")
  }
}

sc.m()
