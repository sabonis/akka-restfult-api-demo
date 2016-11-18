package property.message

/**
  * Created by sabonis on 07/11/2016.
  */
package object response {

  trait BaseResponse {
    val code: Int
    val message: String
  }

  case class Response(code: Int, message: String) extends BaseResponse
  case class RAuthorize(code: Int, message: String, token: String) extends BaseResponse

  case class Properties[A](code: Int, ps: Seq[A])

  case class Reservables[A](code: Int, rs: Seq[A])
  case class Reservable(code: Int = 200, r: Reservable)
}
