// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/bingerso/GitHub-Projects/csc750-project2/bingers-project2/conf/routes
// @DATE:Sat Sep 22 12:40:29 EDT 2018

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:6
package controllers.javascript {

  // @LINE:6
  class ReverseHomeController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:31
    def getSellOffer: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.getSellOffer",
      """
        function(offerID0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "selloffers/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("offerID", offerID0))})
        }
      """
    )
  
    // @LINE:22
    def getTransactions: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.getTransactions",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "transactions"})
        }
      """
    )
  
    // @LINE:19
    def getBalance: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.getBalance",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "getbalance"})
        }
      """
    )
  
    // @LINE:45
    def debugReset: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.debugReset",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "debug/reset"})
        }
      """
    )
  
    // @LINE:42
    def debugConfirmNoResponse: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.debugConfirmNoResponse",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "debug/confirm_no_response"})
        }
      """
    )
  
    // @LINE:16
    def addBalance: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.addBalance",
      """
        function(amount0) {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "addbalance/usd/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Double]].javascriptUnbind + """)("amount", amount0))})
        }
      """
    )
  
    // @LINE:39
    def debugConfirmFail: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.debugConfirmFail",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "debug/confirm_fail"})
        }
      """
    )
  
    // @LINE:25
    def getTransaction: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.getTransaction",
      """
        function(transactionID0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "transactions/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Int]].javascriptUnbind + """)("transactionID", transactionID0))})
        }
      """
    )
  
    // @LINE:28
    def getSellOffers: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.getSellOffers",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "selloffers"})
        }
      """
    )
  
    // @LINE:6
    def index: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.index",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + """"})
        }
      """
    )
  
    // @LINE:34
    def requestBuyTransaction: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.requestBuyTransaction",
      """
        function(maxRate0,amount1) {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "buy/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Double]].javascriptUnbind + """)("maxRate", maxRate0)) + "/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Int]].javascriptUnbind + """)("amount", amount1))})
        }
      """
    )
  
  }

  // @LINE:9
  class ReverseAssets(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:9
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
