package scala

import org.scalatest._

/**
 * Created by Benedict Jin on 2015/9/7.
 */
abstract class UnitTestStyle extends FlatSpec
with Matchers with OptionValues with Inside with Inspectors