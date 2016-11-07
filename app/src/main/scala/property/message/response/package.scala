package property.message

/**
  * Created by sabonis on 07/11/2016.
  */
package object response {

  case class Response(code: Int, message: String)
  case class RAuthorize(code: Int, token: String)
  case class ReservableResp(code: Int, token: String)

}
