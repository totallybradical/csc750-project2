// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/bingerso/GitHub-Projects/csc750-project2/bingers-project2/conf/routes
// @DATE:Sat Sep 22 12:40:29 EDT 2018

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset
import _root_.play.libs.F

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:6
  HomeController_0: controllers.HomeController,
  // @LINE:9
  Assets_1: controllers.Assets,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:6
    HomeController_0: controllers.HomeController,
    // @LINE:9
    Assets_1: controllers.Assets
  ) = this(errorHandler, HomeController_0, Assets_1, "/")

  def withPrefix(prefix: String): Routes = {
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, HomeController_0, Assets_1, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix, """controllers.HomeController.index"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """assets/""" + "$" + """file<.+>""", """controllers.Assets.versioned(path:String = "/public", file:Asset)"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """addbalance/usd/""" + "$" + """amount<[^/]+>""", """controllers.HomeController.addBalance(amount:Double)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """getbalance""", """controllers.HomeController.getBalance"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """transactions""", """controllers.HomeController.getTransactions"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """transactions/""" + "$" + """transactionID<[^/]+>""", """controllers.HomeController.getTransaction(transactionID:Int)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """selloffers""", """controllers.HomeController.getSellOffers"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """selloffers/""" + "$" + """offerID<[^/]+>""", """controllers.HomeController.getSellOffer(offerID:String)"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """buy/""" + "$" + """maxRate<[^/]+>/""" + "$" + """amount<[^/]+>""", """controllers.HomeController.requestBuyTransaction(maxRate:Double, amount:Int)"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """debug/confirm_fail""", """controllers.HomeController.debugConfirmFail"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """debug/confirm_no_response""", """controllers.HomeController.debugConfirmNoResponse"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """debug/reset""", """controllers.HomeController.debugReset"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:6
  private[this] lazy val controllers_HomeController_index0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix)))
  )
  private[this] lazy val controllers_HomeController_index0_invoker = createInvoker(
    HomeController_0.index,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "index",
      Nil,
      "GET",
      this.prefix + """""",
      """ An example controller showing a sample home page""",
      Seq()
    )
  )

  // @LINE:9
  private[this] lazy val controllers_Assets_versioned1_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("assets/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_versioned1_invoker = createInvoker(
    Assets_1.versioned(fakeValue[String], fakeValue[Asset]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "versioned",
      Seq(classOf[String], classOf[Asset]),
      "GET",
      this.prefix + """assets/""" + "$" + """file<.+>""",
      """ Map static resources from the /public folder to the /assets URL path""",
      Seq()
    )
  )

  // @LINE:16
  private[this] lazy val controllers_HomeController_addBalance2_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("addbalance/usd/"), DynamicPart("amount", """[^/]+""",true)))
  )
  private[this] lazy val controllers_HomeController_addBalance2_invoker = createInvoker(
    HomeController_0.addBalance(fakeValue[Double]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "addBalance",
      Seq(classOf[Double]),
      "POST",
      this.prefix + """addbalance/usd/""" + "$" + """amount<[^/]+>""",
      """ Add the given amount to the user's USD balance""",
      Seq()
    )
  )

  // @LINE:19
  private[this] lazy val controllers_HomeController_getBalance3_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("getbalance")))
  )
  private[this] lazy val controllers_HomeController_getBalance3_invoker = createInvoker(
    HomeController_0.getBalance,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "getBalance",
      Nil,
      "GET",
      this.prefix + """getbalance""",
      """ Return the user's USD and BTC balance""",
      Seq()
    )
  )

  // @LINE:22
  private[this] lazy val controllers_HomeController_getTransactions4_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("transactions")))
  )
  private[this] lazy val controllers_HomeController_getTransactions4_invoker = createInvoker(
    HomeController_0.getTransactions,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "getTransactions",
      Nil,
      "GET",
      this.prefix + """transactions""",
      """ Get a list of successful transactions""",
      Seq()
    )
  )

  // @LINE:25
  private[this] lazy val controllers_HomeController_getTransaction5_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("transactions/"), DynamicPart("transactionID", """[^/]+""",true)))
  )
  private[this] lazy val controllers_HomeController_getTransaction5_invoker = createInvoker(
    HomeController_0.getTransaction(fakeValue[Int]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "getTransaction",
      Seq(classOf[Int]),
      "GET",
      this.prefix + """transactions/""" + "$" + """transactionID<[^/]+>""",
      """ Get details of a given transaction""",
      Seq()
    )
  )

  // @LINE:28
  private[this] lazy val controllers_HomeController_getSellOffers6_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("selloffers")))
  )
  private[this] lazy val controllers_HomeController_getSellOffers6_invoker = createInvoker(
    HomeController_0.getSellOffers,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "getSellOffers",
      Nil,
      "GET",
      this.prefix + """selloffers""",
      """ Get a list of sell Offer IDs""",
      Seq()
    )
  )

  // @LINE:31
  private[this] lazy val controllers_HomeController_getSellOffer7_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("selloffers/"), DynamicPart("offerID", """[^/]+""",true)))
  )
  private[this] lazy val controllers_HomeController_getSellOffer7_invoker = createInvoker(
    HomeController_0.getSellOffer(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "getSellOffer",
      Seq(classOf[String]),
      "GET",
      this.prefix + """selloffers/""" + "$" + """offerID<[^/]+>""",
      """ Get sell offer details""",
      Seq()
    )
  )

  // @LINE:34
  private[this] lazy val controllers_HomeController_requestBuyTransaction8_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("buy/"), DynamicPart("maxRate", """[^/]+""",true), StaticPart("/"), DynamicPart("amount", """[^/]+""",true)))
  )
  private[this] lazy val controllers_HomeController_requestBuyTransaction8_invoker = createInvoker(
    HomeController_0.requestBuyTransaction(fakeValue[Double], fakeValue[Int]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "requestBuyTransaction",
      Seq(classOf[Double], classOf[Int]),
      "POST",
      this.prefix + """buy/""" + "$" + """maxRate<[^/]+>/""" + "$" + """amount<[^/]+>""",
      """ Request a buy transaction, for the given BTC amount, at the given maximum rate""",
      Seq()
    )
  )

  // @LINE:39
  private[this] lazy val controllers_HomeController_debugConfirmFail9_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("debug/confirm_fail")))
  )
  private[this] lazy val controllers_HomeController_debugConfirmFail9_invoker = createInvoker(
    HomeController_0.debugConfirmFail,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "debugConfirmFail",
      Nil,
      "POST",
      this.prefix + """debug/confirm_fail""",
      """ After this request is posted, the market actor will reply fail to subsequent Confirm reuqests without actual processing""",
      Seq()
    )
  )

  // @LINE:42
  private[this] lazy val controllers_HomeController_debugConfirmNoResponse10_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("debug/confirm_no_response")))
  )
  private[this] lazy val controllers_HomeController_debugConfirmNoResponse10_invoker = createInvoker(
    HomeController_0.debugConfirmNoResponse,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "debugConfirmNoResponse",
      Nil,
      "POST",
      this.prefix + """debug/confirm_no_response""",
      """ After this request is posted, the market actor will not reply to subsequent Confirm requests and do no actual processing""",
      Seq()
    )
  )

  // @LINE:45
  private[this] lazy val controllers_HomeController_debugReset11_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("debug/reset")))
  )
  private[this] lazy val controllers_HomeController_debugReset11_invoker = createInvoker(
    HomeController_0.debugReset,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "debugReset",
      Nil,
      "POST",
      this.prefix + """debug/reset""",
      """ After this request is posted, the actor will reset to normal operation""",
      Seq()
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:6
    case controllers_HomeController_index0_route(params@_) =>
      call { 
        controllers_HomeController_index0_invoker.call(HomeController_0.index)
      }
  
    // @LINE:9
    case controllers_Assets_versioned1_route(params@_) =>
      call(Param[String]("path", Right("/public")), params.fromPath[Asset]("file", None)) { (path, file) =>
        controllers_Assets_versioned1_invoker.call(Assets_1.versioned(path, file))
      }
  
    // @LINE:16
    case controllers_HomeController_addBalance2_route(params@_) =>
      call(params.fromPath[Double]("amount", None)) { (amount) =>
        controllers_HomeController_addBalance2_invoker.call(HomeController_0.addBalance(amount))
      }
  
    // @LINE:19
    case controllers_HomeController_getBalance3_route(params@_) =>
      call { 
        controllers_HomeController_getBalance3_invoker.call(HomeController_0.getBalance)
      }
  
    // @LINE:22
    case controllers_HomeController_getTransactions4_route(params@_) =>
      call { 
        controllers_HomeController_getTransactions4_invoker.call(HomeController_0.getTransactions)
      }
  
    // @LINE:25
    case controllers_HomeController_getTransaction5_route(params@_) =>
      call(params.fromPath[Int]("transactionID", None)) { (transactionID) =>
        controllers_HomeController_getTransaction5_invoker.call(HomeController_0.getTransaction(transactionID))
      }
  
    // @LINE:28
    case controllers_HomeController_getSellOffers6_route(params@_) =>
      call { 
        controllers_HomeController_getSellOffers6_invoker.call(HomeController_0.getSellOffers)
      }
  
    // @LINE:31
    case controllers_HomeController_getSellOffer7_route(params@_) =>
      call(params.fromPath[String]("offerID", None)) { (offerID) =>
        controllers_HomeController_getSellOffer7_invoker.call(HomeController_0.getSellOffer(offerID))
      }
  
    // @LINE:34
    case controllers_HomeController_requestBuyTransaction8_route(params@_) =>
      call(params.fromPath[Double]("maxRate", None), params.fromPath[Int]("amount", None)) { (maxRate, amount) =>
        controllers_HomeController_requestBuyTransaction8_invoker.call(HomeController_0.requestBuyTransaction(maxRate, amount))
      }
  
    // @LINE:39
    case controllers_HomeController_debugConfirmFail9_route(params@_) =>
      call { 
        controllers_HomeController_debugConfirmFail9_invoker.call(HomeController_0.debugConfirmFail)
      }
  
    // @LINE:42
    case controllers_HomeController_debugConfirmNoResponse10_route(params@_) =>
      call { 
        controllers_HomeController_debugConfirmNoResponse10_invoker.call(HomeController_0.debugConfirmNoResponse)
      }
  
    // @LINE:45
    case controllers_HomeController_debugReset11_route(params@_) =>
      call { 
        controllers_HomeController_debugReset11_invoker.call(HomeController_0.debugReset)
      }
  }
}
