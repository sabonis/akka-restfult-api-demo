package property.message

import java.sql.Timestamp

import property.model

/**
  * Created by sabonis on 07/11/2016.
  */
package object request {

  case class Authorize(companyId: Int, clientId: String, expireDuration: Int, password: String)

  case class Reservable(name: String, meta: Option[String], status: Int, startDate: Timestamp, endDate: Timestamp, propertyId: Int)

  case class UpdateReservable(name: Option[String],
                              meta: Option[String],
                              status: Option[Int],
                              startDate: Option[Timestamp],
                              endDate: Option[Timestamp],
                              propertyId: Option[Int]) extends UpdateEntity[model.Reservable] {

    def merge(r: model.Reservable): model.Reservable = {
      model.Reservable(r.id, name.getOrElse(r.name), meta, status.getOrElse(r.status),
        startDate.getOrElse(r.startDate), endDate.getOrElse(r.endDate), propertyId.getOrElse(r.propertyId))
    }
  }

  case class UpdateProperty(name: Option[String]) extends UpdateEntity[model.Property] {
    override def merge(p: model.Property): model.Property = model.Property(p.id, name.getOrElse(p.name))
  }

  case class LockReservable(reservableId: Int, lockdownSeconds: Int)

  case class Property(name: String)

  trait UpdateEntity[A] {
    def merge(model: A): A
  }

}
