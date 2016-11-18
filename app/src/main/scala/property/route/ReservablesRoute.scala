package property.route

import java.sql.Timestamp

import akka.actor.ActorSystem
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.Directives._
import akka.pattern
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import property.message.response.Response
import property.message.{request, response}
import property.model._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by sabonis on 09/11/2016.
  */
object ReservablesRoute extends Route {

  def apply()(implicit ec: ExecutionContext, sys: ActorSystem) = {
    pathPrefix("reservables") {
      val parser = ISODateTimeFormat.dateTimeParser()
      parameters('startDate, 'endDate) { (startDate, endDate) =>
        complete {
          val startTs = new Timestamp(parser.parseDateTime(startDate).getMillis)
          val endTs = new Timestamp(parser.parseDateTime(endDate).getMillis)
          //Reservables.table.filter(r => r.startDate <= startTs && r.endDate >= endTs).result map (_.toString)
          "TODO"
        }
      } ~
      path(IntNumber) { rId =>
        get {
          complete {
            Reservables.read(rId) map {
              case Some(r) => ToResponseMarshallable(r)
              case None => ToResponseMarshallable(Response(200, "Devleoper is lazy"))
            }
          }
        } ~
          delete {
            complete {
              Reservables.deleteById(rId) map int2Response
            }
          }
      } ~
      get {
        complete {
          Reservables.all map (rs => response.Reservables(200, rs))
        }
      } ~
      post {
        entity(as[request.Reservable]) { r =>
          complete {
            Reservables.create(Reservable(
              0, r.name, r.meta, r.status, r.startDate, r.endDate, r.propertyId)
            ) map int2Response
          }
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
            Reservables.lock(lr.reservableId) map int2Response
          }
        }
      }
    }
  }

  // Once for all conversion from DateTime to Timestamp, makes my life more easier
  implicit def dateTime2Timestamp(td: DateTime): Timestamp = new Timestamp(td.getMillis)

}
