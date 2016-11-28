package property

import java.sql.Timestamp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import org.joda.time.DateTime
import property.message._
import property.message.request.Authorize
import property.message.response.{RAuthorize, Response}
import property.model._
import property.route.{PropertiesRoute, ReservablesRoute}
import slick.dbio.DBIOAction
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import scala.io.StdIn

/**
  * Created by sabonis on 03/11/2016.
  */
object WebServer extends JsonSupport {

  val Token = "0909"
  val database = Database.forConfig("postgres")

  // needed for the future flatMap/onComplete in the end
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  def main(args: Array[String]) {

    initDB()

    val route =
      path("hello") {
        // Here is a playground.
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
          //complete("fuck")
        }
      } ~
      path("login") {
        post {
          entity(as[Authorize]) { auth =>
            complete(RAuthorize(200, "ok", Token))
          }
        }
      } ~
      ReservablesRoute() ~
      PropertiesRoute()

    val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => {
        database.close()
        system.terminate()
      }) // and shutdown when done
  }

  def initDB() = {
    db {
      //val dts = new Timestamp(new DateTime(2016, 11, 10, 0, 0, 0, 0).getMillis)
      //val dte = new Timestamp(new DateTime(2016, 11, 20, 0, 0, 0, 0).getMillis)
      DBIO.seq(
        Reservables.tableQuery.schema.create,
        Properties.tableQuery.schema.create
        //reservables.map(r => (r.name, r.startDate, r.endDate, r.propertyId)) += ("RAV4", dts, dte, 2),
        //reservables.map(r => (r.name, r.startDate, r.endDate, r.propertyId)) += ("RAV5", dts, dte, 2)
        //properties.map(_.name) += "testkk",
        //properties.map(_.name) += "sabonis"
      )
    }.onFailure {
      case e => println(e)
    }
  }

  def db[R](dbAction: => DBIOAction[R, NoStream, Nothing]): Future[R] = {
    database run dbAction
  }

}
