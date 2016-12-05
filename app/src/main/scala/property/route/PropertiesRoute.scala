package property.route

import akka.actor.ActorSystem
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.Directives._
import property.message.response.Response
import property.message.{request, response}
import property.model.{Properties, Property}

import scala.concurrent.ExecutionContext

/**
  * Created by sabonis on 18/11/2016.
  */
object PropertiesRoute extends Route {

  def apply()(implicit ec: ExecutionContext, sys: ActorSystem) = {
    pathPrefix("properties") {
      // /properties/ID
      path(IntNumber) { pid =>
        get {
          complete {
            Properties.read(pid) map {
              case Some(p) => ToResponseMarshallable(p)
              case None => ToResponseMarshallable(Response(200, "Devleoper is lazy"))
            }
          }
        } ~
        delete {
          complete {
            Properties.deleteById(pid) map int2Response
          }
        } ~
        put {
          entity(as[request.Property]) { p =>
            complete {
              Properties.updateById(pid, Property(pid, p.name)) map int2Response
            }
          }
        } ~
        patch {
          entity(as[request.UpdateProperty]) { p =>
            complete {
              Properties.update(pid, p) map int2Response
            }
          }
        }
      } ~
      get {
        complete {
          Properties.all map { ps =>
            response.Properties(200, ps)
          }
        }
      } ~
      post {
        entity(as[request.Property]) { p =>
          complete {
            Properties.create(Property(0, p.name)) map { r =>
              response.Response(200, r.toString)
            }
          }
        }
      }
    }
  }

}
