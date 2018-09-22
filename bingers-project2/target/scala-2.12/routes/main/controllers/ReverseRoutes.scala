// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/bingerso/GitHub-Projects/csc750-project2/bingers-project2/conf/routes
// @DATE:Sat Sep 22 12:40:29 EDT 2018

import play.api.mvc.Call


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:6
package controllers {

  // @LINE:6
  class ReverseHomeController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:31
    def getSellOffer(offerID:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "selloffers/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("offerID", offerID)))
    }
  
    // @LINE:22
    def getTransactions(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "transactions")
    }
  
    // @LINE:19
    def getBalance(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "getbalance")
    }
  
    // @LINE:45
    def debugReset(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "debug/reset")
    }
  
    // @LINE:42
    def debugConfirmNoResponse(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "debug/confirm_no_response")
    }
  
    // @LINE:16
    def addBalance(amount:Double): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "addbalance/usd/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Double]].unbind("amount", amount)))
    }
  
    // @LINE:39
    def debugConfirmFail(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "debug/confirm_fail")
    }
  
    // @LINE:25
    def getTransaction(transactionID:Int): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "transactions/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Int]].unbind("transactionID", transactionID)))
    }
  
    // @LINE:28
    def getSellOffers(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "selloffers")
    }
  
    // @LINE:6
    def index(): Call = {
      
      Call("GET", _prefix)
    }
  
    // @LINE:34
    def requestBuyTransaction(maxRate:Double, amount:Int): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "buy/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Double]].unbind("maxRate", maxRate)) + "/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Int]].unbind("amount", amount)))
    }
  
  }

  // @LINE:9
  class ReverseAssets(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:9
    def versioned(file:Asset): Call = {
      implicit lazy val _rrc = new play.core.routing.ReverseRouteContext(Map(("path", "/public"))); _rrc
      Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[play.api.mvc.PathBindable[Asset]].unbind("file", file))
    }
  
  }


}
