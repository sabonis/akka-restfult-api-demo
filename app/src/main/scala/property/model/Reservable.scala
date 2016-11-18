package property.model

import java.sql.Timestamp

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

/**
  * Created by sabonis on 07/11/2016.
  */
case class Reservable (
  id: Int, name: String, meta: Option[String], status: Int, startDate: Timestamp, endDate: Timestamp, propertyId: Int
) extends BaseEntity

class ReservablesDAO extends SqlDAO[Reservable] {
  val StatusFree = 0
  val StatusLock = 1
  val StatusSold = 2

  val tableQuery = TableQuery[TableImpl]


  def lock(id: Int) = updateStatus(id, StatusLock)

  def unlock(id: Int) = updateStatus(id, StatusFree)

  private def updateStatus(id: Int, status: Int) = {
    db run {
      tableQuery.filter(_.id === id)
        .map(_.status)
        .update(status)
    }
  }

  override def create(v: Reservable): Future[Int] = {
    db run {
      tableQuery.map(r => (r.name, r.startDate, r.endDate, r.propertyId)) += (v.name, v.startDate, v.endDate, v.propertyId)
    }
  }

  class TableImpl(tag: Tag) extends BaseTable[Reservable](tag, "RESERVABLE") {
    //override def id = column[Int]("ID")
    def name = column[String]("NAME")
    def meta = column[Option[String]]("META")
    def status = column[Int]("STATUS", O.Default(StatusFree))
    def startDate = column[Timestamp]("START_DATE")
    def endDate = column[Timestamp]("END_DATE")
    def propertyId = column[Int]("PROPERTY_ID")

    // Every table needs a * projection with the same type as the table's type parameter
    def * = (id, name, meta, status, startDate, endDate, propertyId) <> (Reservable.tupled, Reservable.unapply)

    def property = foreignKey("Reservable", propertyId, properties)(_.id)

    //def timestamp = new Timestamp(Calendar.getInstance().getTime.getTime)

    lazy val properties = Properties.tableQuery
  }
}

