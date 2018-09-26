// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/bingerso/GitHub-Projects/csc750-project2/bingers-project2/conf/routes
// @DATE:Mon Sep 24 21:13:39 EDT 2018

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:6
package controllers.javascript {

  // @LINE:13
  class ReverseHomeController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:28
    def getSellOffer: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.getSellOffer",
      """
        function(offerID0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "selloffers/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("offerID", offerID0))})
        }
      """
    )
  
    // @LINE:19
    def getTransactionIDs: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.getTransactionIDs",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "transactions"})
        }
      """
    )
  
    // @LINE:16
    def getBalances: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.getBalances",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "getbalance"})
        }
      """
    )
  
    // @LINE:42
    def debugReset: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.debugReset",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "debug/reset"})
        }
      """
    )
  
    // @LINE:13
    def addBalanceUSD: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.addBalanceUSD",
      """
        function(amount0) {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "addbalance/usd/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Double]].javascriptUnbind + """)("amount", amount0))})
        }
      """
    )
  
    // @LINE:39
    def debugConfirmNoResponse: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.debugConfirmNoResponse",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "debug/confirm_no_response"})
        }
      """
    )
  
    // @LINE:36
    def debugConfirmFail: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.debugConfirmFail",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "debug/confirm_fail"})
        }
      """
    )
  
    // @LINE:25
    def getSellOfferIDs: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.getSellOfferIDs",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "selloffers"})
        }
      """
    )
  
    // @LINE:22
    def getTransaction: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.getTransaction",
      """
        function(transactionID0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "transactions/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Int]].javascriptUnbind + """)("transactionID", transactionID0))})
        }
      """
    )
  
    // @LINE:31
    def requestBuyTransaction: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.requestBuyTransaction",
      """
        function(maxRate0,amount1) {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "buy/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Double]].javascriptUnbind + """)("maxRate", maxRate0)) + "/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Int]].javascriptUnbind + """)("amount", amount1))})
        }
      """
    )
  
  }

  // @LINE:6
  class ReverseAssets(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:6
    def versioned: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Assets.versioned",
      """
        function(file1) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[play.api.mvc.PathBindable[Asset]].javascriptUnbind + """)("file", file1)})
        }
      """
    )
  
  }


}
