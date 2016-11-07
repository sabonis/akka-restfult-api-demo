package property.model

import slick.jdbc.PostgresProfile.api._

/**
  * Created by sabonis on 03/11/2016.
  */
case class Property(id: Int, name: String) {

}

class PropertiesDAO extends SqlDAO[Property] {
  val tableQuery = TableQuery[TableImpl]

  class TableImpl(tag: Tag) extends Table[Property](tag, "PROPERTIES") {
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc) // This is the primary key column
    def name = column[String]("NAME")
    // Every table needs a * projection with the same type as the table's type parameter
    def * = (id, name) <> (Property.tupled, Property.unapply)
  }
}

object PropertiesDAO {
}

