package property.message

import java.sql.Timestamp

/**
  * Created by sabonis on 07/11/2016.
  */
package object request {

  case class Authorize(companyId: Int, clientId: String, expireDuration: Int, password: String)

  case class Reservable(name: String, meta: Option[String], status: Int, startDate: Timestamp, endDate: Timestamp, propertyId: Int)

  case class LockReservable(reservableId: Int, lockdownSeconds: Int)

  case class Property(name: String)

}
