package controllers

import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def version = Action {
    import info.BuildInformation
    Ok(s"v${BuildInformation.version}, b${BuildInformation.buildDate}, r${BuildInformation.rev}")
  }

}