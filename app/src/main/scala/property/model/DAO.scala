package property.model

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

/**
  * Created by sabonis on 11/11/2016.
  */
sealed trait DAO[M] {
  def all: Future[Seq[M]]
  def updateById(id: Int, value: M): Future[Int]
}

trait SqlDAO[M <: BaseEntity] extends DAO[M] {

  val tableQuery: TableQuery[_ <: BaseTable[M]]

  protected val db = Database.forConfig("postgres")

  override def all: Future[Seq[M]] = db run tableQuery.result

  override def updateById(id: Int, value: M): Future[Int] = {
    db run
      tableQuery
        .filter(_.id === id)
        .update(value)
  }


}

