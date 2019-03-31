val service = new ServiceImportant("Worker")
val serviceWithLogger = new ServiceImportant("Worker with Logger") with StdoutLogger
(1 to 3) foreach (i => println(s"${service.work(i)}"))
val serviceWithLogger2 = new ServiceImportant("Worker with Logger") with StdoutLogger {
  override def work(i: Int): Int = {
    info("Working...")
    val result = super.work(i)
    info("Worked.")
    result
  }
}

trait Logger {
  def info(message: String): Unit

  def warn(message: String): Unit

  def error(message: String): Unit
}

trait StdoutLogger extends Logger {
  override def info(message: String): Unit = println(s"INFO: $message")

  override def warn(message: String): Unit = println(s"WARN: $message")

  override def error(message: String): Unit = println(s"ERROR: $message")
}

(1 to 3) foreach (i => println(s"${serviceWithLogger.work(i)}"))

class ServiceImportant(name: String) {
  def work(i: Int): Int = {
    println(s"Work: $i")
    i + 1
  }
}

(1 to 3) foreach (i => println(s"${serviceWithLogger2.work(i)}"))
