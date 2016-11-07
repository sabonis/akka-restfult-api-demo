package property.model

import java.sql.Timestamp

import slick.jdbc.PostgresProfile.api._

/**
  * Created by sabonis on 07/11/2016.
  */
case class Reservable(
  id: Int, name: String, meta: Option[String], status: Int, startDate: Timestamp, endDate: Timestamp, propertyId: Int
)

class ReservablesDAO extends SqlDAO[Reservable] {
  val tableQuery = TableQuery[TableImpl]

  class TableImpl(tag: Tag) extends Table[Reservable](tag, "RESERVABLE") {
    val StatusFree = 0
    val StatusLock = 1
    val StatusSold = 2

    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc) // This is the primary key column
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

