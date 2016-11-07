package property.route

import java.sql.Timestamp

import akka.http.scaladsl.server.Directives._
import org.joda.time.format.ISODateTimeFormat
import property.message.JsonSupport
import property.model._

import scala.concurrent.ExecutionContext

/**
  * Created by sabonis on 09/11/2016.
  */
object ReservablesRoute extends JsonSupport {

  def apply()(implicit ec: ExecutionContext) = {
    path("reservables") {
      val parser = ISODateTimeFormat.dateTimeParser()
      parameters('startDate, 'endDate) { (startDate, endDate) =>
        complete {
          val startTs = new Timestamp(parser.parseDateTime(startDate).getMillis)
          val endTs = new Timestamp(parser.parseDateTime(endDate).getMillis)
          //Reservables.table.filter(r => r.startDate <= startTs && r.endDate >= endTs).result map (_.toString)
          "TODO"
        }
      } ~
      get {
        complete {
          Reservables.all map (_.toString)
        }
      }
    }
  }

}
