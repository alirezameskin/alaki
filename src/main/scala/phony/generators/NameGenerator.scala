package phony.generators

import cats.Monad
import cats.implicits._
import phony.{Locale, RandomUtility}

class NameGenerator[F[_]: Monad](val utility: RandomUtility)(implicit locale: Locale[F]) {
  def firstName: F[String] =
    locale.name.map(_.firstNames).map(utility.randomItem)

  def lastName: F[String] =
    locale.name.map(_.lastNames).map(utility.randomItem)

  def prefix: F[String] =
    locale.name.map(_.prefixes).map(utility.randomItem)

  def suffix: F[String] =
    locale.name.map(_.suffixes).map(utility.randomItem)

  def fullName: F[String] = fullName(false, false)

  def fullName(withPrefix: Boolean = false, withSuffix: Boolean = false): F[String] =
    (withPrefix, withSuffix) match {
      case (true, true) => (prefix, firstName, lastName, suffix).mapN(combine4(" "))
      case (true, false) => (prefix, firstName, lastName).mapN(combine3(" "))
      case (false, true) => (firstName, lastName, suffix).mapN(combine3(" "))
      case (false, false) => (firstName, lastName).mapN(combine2(" "))
    }

  def username: F[String] =
    for {
      first <- locale.name.map(_.firstNames).map(utility.randomItem)
      last <- locale.name.map(_.lastNames).map(utility.randomItem)
      rand <- utility.nextInt(1000).pure[F]
    } yield s"${first}_${last}_${rand}".toLowerCase

  private def combine4(glue: String)(p1: String, p2: String, p3: String, p4: String) =
    List(p1, p2, p3, p4).mkString(glue).trim

  private def combine3(glue: String)(p1: String, p2: String, p3: String) =
    List(p1, p2, p3).mkString(glue).trim

  private def combine2(glue: String)(p1: String, p2: String) =
    List(p1, p2).mkString(glue).trim
}