package bootstrap

import java.util.UUID

import info.BuildInformation
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._
import play.api.{Application, Logger}

import scala.concurrent.Future

/**
 * Created by alaym on 07/11/14.
 */
object AccessLoggingFilter extends Filter {

  override def apply(f: (RequestHeader) => Future[Result])(rh: RequestHeader): Future[Result] = {

    val arrival = System.currentTimeMillis()

    val uuid = UUID.randomUUID().toString

    val eventualResult = f(rh)

    eventualResult map { result =>

      val duration = System.currentTimeMillis() - arrival

      if (Logger.isDebugEnabled) {

        val reqLog = s"\n> ${rh.method} ${rh.uri}${rh.headers.toSimpleMap.map(l => s"> ${l._1}: ${l._2}").mkString("\n", "\n", "\n")}"
        val resLog = s"\n< ${result.header.status}${result.header.headers.map(l => s"< ${l._1}: ${l._2}").mkString("\n", "\n", "\n")}"

        Logger.debug(s"$uuid - $reqLog$resLog")

      }

      val line = s"$uuid - ${rh.method} ${rh.uri} ${result.header.status} ${duration}ms"

      Logger.info(line)

      result

    }

  }

}

object Global extends WithFilters(AccessLoggingFilter) {

  override def onStart(app: Application): Unit = {

    Logger.info(s"Vpp Butterfly ${BuildInformation.version} started!")

  }

  override def onStop(app: Application): Unit = {

    Logger.info(s"Vpp Butterfly ${BuildInformation.version} stopped!")

  }

}
