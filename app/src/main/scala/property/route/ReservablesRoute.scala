package property.route

import java.sql.Timestamp

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.pattern
import org.joda.time.format.ISODateTimeFormat
import property.message.{JsonSupport, request, response}
import property.model._

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

/**
  * Created by sabonis on 09/11/2016.
  */
object ReservablesRoute extends JsonSupport {

  def apply()(implicit ec: ExecutionContext, sys: ActorSystem) = {
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
    } ~
    path("lockReservable") {
      post {
        entity(as[request.LockReservable]) { lr =>

          // unlock after specified time
          pattern.after(lr.lockdownSeconds seconds, sys.scheduler) {
            Future {
              Reservables.unlock(lr.reservableId)
            }
          }

          complete {
            Reservables.lock(lr.reservableId).map {
              v: Int => response.Response(200, v.toString)
              //response.Response(500, e.toString)
            }
          }
        }
      }
    }
  }

}
