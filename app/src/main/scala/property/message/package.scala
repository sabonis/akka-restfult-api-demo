package property

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import property.message.request.Authorize
import property.message.response._
import property.model.Property
import spray.json.{DefaultJsonProtocol, JsonFormat}

/**
  * Created by sabonis on 07/11/2016.
  */
package object message {

  trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val authorizeFormat = jsonFormat4(Authorize)
    implicit val responseFormat = jsonFormat2(Response)
    implicit val rAuthorizeFormat = jsonFormat2(RAuthorize)
    implicit val propertFormat = jsonFormat2(Property)
    implicit def propertiesFormat[A: JsonFormat] = jsonFormat2(Properties[A])

    implicit val lockerReservableFormat = jsonFormat2(request.LockReservable)

  }

}
