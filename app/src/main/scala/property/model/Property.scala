package property.model

import slick.jdbc.PostgresProfile.api._

/**
  * Created by sabonis on 03/11/2016.
  */
case class Property(id: Int, name: String) extends BaseEntity

class PropertiesDAO extends SqlDAO[Property] {
  val tableQuery = TableQuery[TableImpl]

  class TableImpl(tag: Tag) extends BaseTable[Property](tag, "PROPERTIES") {
    def name = column[String]("NAME")
    // Every table needs a * projection with the same type as the table's type parameter
    def * = (id, name) <> (Property.tupled, Property.unapply)
  }
}

object PropertiesDAO {
}

