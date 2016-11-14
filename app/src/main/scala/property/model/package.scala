package property

/**
  * Created by sabonis on 12/11/2016.
  */
package object model {

  trait BaseEntity {
    val id: Int
  }

  import slick.jdbc.PostgresProfile.api._
  abstract class BaseTable[M <: BaseEntity](tag: Tag, name: String) extends Table[M](tag, name) {
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc) // This is the primary key column
  }

  object Reservables extends ReservablesDAO

  object Properties extends PropertiesDAO
}
