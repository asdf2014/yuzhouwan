for (s <- Seq(1, 2, 3, 4)) {
  s match {
    case _ if s % 2 == 0 => println(s"even: $s")
    case _ => println(s"odd: $s")
  }
}
