package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.Messages
import model.Medication

object Medications extends Controller {

  val medication = Form(
    mapping(
      "mobile" -> nonEmptyText,
      "reminderstart" -> date("dd/MM/yyyy"),
      "reminderend" -> date("dd/MM/yyyy"),
      "schedule" -> nonEmptyText,
      "message" -> nonEmptyText
    )(Medication.apply)(Medication.unapply)
  )

  def index = Action { implicit request =>
    Ok(views.html.medications.index())
  }

  def reminder = Action { implicit request =>
    medication.bindFromRequest.fold(
      errors => {
        Redirect(routes.Medications.index()).flashing(
          "error" -> Messages(errors.errors.head.message)
        )
      },
      meds => {
        api.Api.medicationReminder(meds)
        Redirect(routes.Medications.index()).flashing(
          "message" -> "Successfully scheduled medication reminder"
        )
      }
    )
  }

}