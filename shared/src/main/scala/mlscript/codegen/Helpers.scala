package mlscript.codegen

import mlscript._, mlscript.utils.shorthands._
import scala.collection.immutable.{Map, Set}

object Helpers {
  /**
    * Show how a term is actually structured.
    */
  def inspect(t: Term): Str = t match {
    case Var(name)     => s"Var($name)"
    case Lam(lhs, rhs) => s"Lam(${inspect(lhs)}, ${inspect(rhs)})"
    case App(lhs, rhs) => s"App(${inspect(lhs)}, ${inspect(rhs)})"
    case Tup(fields) =>
      val entries = fields map {
        case (S(name), value) => s"$name: ${inspect(value)}"
        case (N, value)       => s"_: ${inspect(value)}"
      }
      s"Tup(${entries mkString ", "})"
    case Rcd(fields) =>
      val entries = fields.iterator
        .map { case k -> v => s"${k.name} = ${inspect(v)}" }
        .mkString(", ")
      s"Rcd($entries})"
    case Sel(receiver, fieldName)    => s"Sel(${inspect(receiver)}, $fieldName)"
    case Let(isRec, name, rhs, body) => s"Let($isRec, $name, ${inspect(rhs)}, ${inspect(body)})"
    case Asc(trm, ty)                => s"Asc(${inspect(trm)}, $ty)"
    case CaseOf(trm, cases) =>
      def inspectCaseBranches(br: CaseBranches): Str = br match {
        case Case(clsNme, body, rest) =>
          s"Case($clsNme, ${inspect(t)}, ${inspectCaseBranches(rest)})"
        case Wildcard(body) => s"Wildcard(${inspect(body)})"
        case NoCases        => "NoCases"
      }
      s"CaseOf(${inspect(trm)}, ${inspectCaseBranches(cases)}"
    case IntLit(value)  => s"IntLit($value)"
    case DecLit(value)  => s"DecLit($value)"
    case StrLit(value)  => s"StrLit($value)"
    case UnitLit(value)  => s"UnitLit($value)"
    case Subs(arr, idx) => s"Subs(${inspect(arr)}, ${inspect(idx)})"
  }
}
