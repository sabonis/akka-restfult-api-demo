package property.model

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

/**
  * Created by sabonis on 11/11/2016.
  */
sealed trait DAO[M] {
  def all: Future[Seq[M]]
}

trait SqlDAO[M] extends DAO[M] {
  val tableQuery: TableQuery[_ <: Table[M]]

  private val db = Database.forConfig("postgres")

  def all = db run tableQuery.result
}

