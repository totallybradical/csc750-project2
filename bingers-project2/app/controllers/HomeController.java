package controllers;

import actors.MarketActor;
import actors.UserActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import play.mvc.*;
import play.db.*;
import java.sql.*;
import javax.inject.Inject;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    final ActorRef userActor;
    final ActorRef marketActor;
    final Database db;

    // ======= CONSTRUCTOR =======
    @Inject
    public HomeController(ActorSystem system, Database db) {
        userActor = system.actorOf(UserActor.getProps());
        marketActor = system.actorOf(MarketActor.getProps());

        // Reset database
        this.db = db;
        String clear_eventslog_table = "DELETE FROM eventslog;";
        String clear_orderbook_table = "DELETE FROM orderbook;";
        String initialize_orderbook = "INSERT INTO orderbook VALUES (100.00, 5.0, '431671cb'), (80.00, 2.0, '16b961ed'), (50.00, 12.0, '1e06381d');";
        String clear_transactions_table = "DELETE FROM transactions;";
        try {
            Connection conn = db.getConnection();
            Statement stmt = conn.createStatement();
            // Clear eventslog table
            stmt.execute(clear_eventslog_table);
            // Clear orderbook table
            stmt.execute(clear_orderbook_table);
            // Initialize orderbook table
            stmt.execute(initialize_orderbook);
            // Clear transactions table
            stmt.execute(clear_transactions_table);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    // ======= MAIN FUNCTIONS =======

    public Result addBalance(double amount) {
        return ok("add balance amount: " + String.format("%.2f", amount));
    }

    public Result getBalance() {
        return ok("get current balance");
    }

    public Result getTransactions() {
        return ok("get all transactions");
    }

    public Result getTransaction(int transactionID) {
        return ok("get transaction ID: " + transactionID);
    }

    public Result getSellOffers() {
        return ok("get all sell offers");
    }

    public Result getSellOffer(String offerID) {
        return ok("get sell offer: " + offerID);
    }

    public Result requestBuyTransaction(double maxRate, int amount) {
        return ok("request buy transaction with max rate: " + maxRate + " and amount: " + amount);
    }

    // ======= DEBUG FUNCTIONS =======

    public Result debugConfirmFail() {
        return ok("debug confirm fail requested");
    }

    public Result debugConfirmNoResponse() {
        return ok("debug confirm no response requested");
    }

    public Result debugReset() {
        return ok("debug reset requested");
    }
}
