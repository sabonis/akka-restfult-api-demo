package property

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import property.message.request.Authorize
import property.message.response._
import spray.json.DefaultJsonProtocol

/**
  * Created by sabonis on 07/11/2016.
  */
package object message {

  trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val authorizeFormat = jsonFormat4(Authorize)
    implicit val responseFormat = jsonFormat2(Response)
    implicit val rAuthorizeFormat = jsonFormat2(RAuthorize)
  }

}
