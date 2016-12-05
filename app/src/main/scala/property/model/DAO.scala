package property.model

import property.message.request.UpdateEntity
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

/**
  * Created by sabonis on 11/11/2016.
  */
sealed trait DAO[M] {
  def all: Future[Seq[M]]
  def create(v: M): Future[Int]
  def read(id: Int): Future[Option[M]]
  def updateById(id: Int, value: M): Future[Int]
  def deleteById(id: Int): Future[Int]
}

abstract class SqlDAO[M <: BaseEntity] extends DAO[M] {

  //type Table = BaseTable[M]

  val tableQuery: TableQuery[_ <: BaseTable[M]]
  protected val db = SqlDAO.db
  implicit val ec = db.ioExecutionContext

  def update(id: Int, ue: UpdateEntity[M]): Future[Int] = {
    read(id).flatMap {
      case Some(r) =>
        updateById(id, ue.merge(r))
      case None => Future(0)
    }
  }

  override def all: Future[Seq[M]] = db run tableQuery.result

  override def updateById(id: Int, value: M): Future[Int] = {
    db run
      tableQuery
        .filter(_.id === id)
        .update(value)
  }

  override def deleteById(id: Int): Future[Int] = {
    db run
      tableQuery
        .filter(_.id === id)
        .delete
  }

  override def read(id: Int) = {
    db run {
      tableQuery
        .filter(_.id === id).result
        .headOption
    }

  }

}

object SqlDAO {
  protected val db = Database.forConfig("postgres")
}

