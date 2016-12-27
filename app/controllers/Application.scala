package controllers

import play.api._
import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current
import play.api.libs.json.Json
import play.api.db._
import java.util.Base64
import play.api.Logger

import models.Calculator

object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def calculus(query: String) = Action { request =>

    try {

      // Decode the given string (Base64, UTF-8)
      val decoded: String = new String(Base64.getDecoder().decode(query))

      // convert query to rpn format
      val rpn = Calculator.infixToRPN(decoded.replaceAll("\\s", ""))

      // calculate
      val result = Calculator.evalRPN(rpn)

      // Create Json object with the result of the operation
      val data = Json.obj("result" -> result) 

      // Send
      Ok(Json.toJson(data))

      // in case of any error, send error json blob
    } catch {
      case e:Exception => {
        e.printStackTrace
        Ok(
          Json.toJson(
              Json.obj(
                "error" -> "invalid operands found",
                "message" -> "supported operators are: + - / * ( )"
                )
            )
        )
      }
    }

}

}