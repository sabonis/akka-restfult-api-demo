package property

import java.sql.Timestamp

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import property.message.request.{Authorize, UpdateProperty, UpdateReservable}
import property.message.response._
import property.model.{Property, Reservable}
import spray.json._

/**
  * Created by sabonis on 07/11/2016.
  */
package object message {

  /**
    * json two way conversion
    */
  trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
    implicit object DateTimeFormat extends RootJsonFormat[DateTime] {

      val formatter = ISODateTimeFormat.basicDateTimeNoMillis

      def write(obj: DateTime): JsValue = {
        JsString(formatter.print(obj))
      }

      def read(json: JsValue): DateTime = json match {
        case JsString(s) => try {
          formatter.parseDateTime(s)
        }
        catch {
          case t: Throwable => error(s)
        }
        case _ =>
          error(json.toString())
      }

      def error(v: Any): DateTime = {
        val example = formatter.print(0)
        deserializationError(f"'$v' is not a valid date value. Dates must be in compact ISO-8601 format, e.g. '$example'")
      }
    }

    implicit object TimestampFormat extends RootJsonFormat[Timestamp] {
      override def write(obj: Timestamp): JsValue = JsNumber(obj.getTime)
      override def read(json: JsValue): Timestamp = {
        try {
          new Timestamp(json.toString().toLong)
        } catch {
          case e: Throwable => deserializationError(e.toString)
        }
      }
    }

    implicit val responseFormat = jsonFormat2(Response)

    implicit val authorizeFormat = jsonFormat4(Authorize)
    implicit val rAuthorizeFormat = jsonFormat3(RAuthorize)

    implicit val propertyRequestFormat = jsonFormat1(request.Property)
    implicit val propertyFormat = jsonFormat2(Property)
    implicit val updatePropertyFormat = jsonFormat1(UpdateProperty)
    implicit def propertiesFormat[A: JsonFormat] = jsonFormat2(Properties[A])

    implicit val lockerReservableFormat = jsonFormat2(request.LockReservable)
    implicit val requestReservableFormat = jsonFormat6(request.Reservable.apply)
    implicit val reservableFormat = jsonFormat7(Reservable)
    implicit def reservablesFormat[A: JsonFormat] = jsonFormat2(Reservables[A])
    implicit val updateReservableFormat = jsonFormat6(UpdateReservable)
  }
}
