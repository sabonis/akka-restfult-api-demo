package property.route

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import property.message.{request, response}
import property.model.{Properties, Property}

import scala.concurrent.ExecutionContext

/**
  * Created by sabonis on 18/11/2016.
  */
object PropertiesRoute extends Route {

  def apply()(implicit ec: ExecutionContext, sys: ActorSystem) = {
    path("properties") {
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
