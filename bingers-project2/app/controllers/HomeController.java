package controllers;

import actors.MarketActor;
import actors.MarketActorProtocol;
import actors.UserActor;
import actors.UserActorProtocol;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.mvc.*;
import play.db.*;
import scala.compat.java8.FutureConverters;

import java.sql.*;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import javax.inject.Singleton;

import static akka.pattern.Patterns.ask;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@Singleton
public class HomeController extends Controller {

    final ActorRef userActor;
    final ActorRef marketActor;
    final Database db;

    // ======= CONSTRUCTOR =======
    @Inject
    public HomeController(ActorSystem system, Database db) {
        userActor = system.actorOf(UserActor.props(1, 0.0, 0.0), "user-actor");
        marketActor = system.actorOf(MarketActor.props(), "market-actor");

        // Reset database
        this.db = db;
        String clear_eventslog_table = "DELETE FROM eventslog;";
        String clear_orderbook_table = "DELETE FROM orderbook;";
        String clear_transactions_table = "DELETE FROM transactions;";
        String clear_users_table = "DELETE FROM users;";
        String clear_accounts_table = "DELETE FROM accounts;";
        String clear_holds_table = "DELETE FROM holds;";
        String reset_counters = "DELETE FROM sqlite_sequence WHERE name='eventslog' OR name='orderbook' OR name='transactions' OR name='users' OR name='accounts' OR name='holds';";
        String initialize_users = "INSERT INTO users (name, email) VALUES ('Bradford Ingersoll', 'bingers@ncsu.edu');";
        String initialize_accounts = "INSERT INTO accounts (userID, accountType, balance) VALUES (1, 'usd', 0.0), (1, 'btc', 0.0);";
        String initialize_orderbook = "INSERT INTO orderbook (rate, amount, offerID) VALUES (100.00, 5.0, '431671cb'), (80.00, 2.0, '16b961ed'), (50.00, 12.0, '1e06381d');";
        try {
            Connection conn = db.getConnection();
            Statement stmt = conn.createStatement();
            // Clear eventslog table
            // stmt.execute(clear_eventslog_table); // Not clearing per TA comment on Piazza
            // Clear orderbook table
            stmt.execute(clear_orderbook_table);
            // Clear transaction table
            stmt.execute(clear_transactions_table);
            // Clear users table
            stmt.execute(clear_users_table);
            // Clear accounts table
            stmt.execute(clear_accounts_table);
            // Clear accounts table
            stmt.execute(clear_holds_table);
            // Reset counters
            stmt.execute(reset_counters);
            // Initialize orderbook table
            stmt.execute(initialize_orderbook);
            // Initialize users table
            stmt.execute(initialize_users);
            // Initialize accounts table
            stmt.execute(initialize_accounts);

            System.out.println("Database initialized");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    // ======= MAIN FUNCTIONS =======

    public CompletionStage<Result> addBalanceUSD(double amount) {
        // userID is hardcoded to 1 for this project (only 1 user)
        return FutureConverters.toJava(ask(userActor, new UserActorProtocol.AddBalanceUSD(db, 1, amount), 1000))
                .thenApply(response -> ok((ObjectNode) response));
    }

    public CompletionStage<Result> getBalances() {
        // userID is hardcoded to 1 for this project (only 1 user)
        return FutureConverters.toJava(ask(userActor, new UserActorProtocol.GetBalances(db, 1), 1000))
                .thenApply(response -> ok((ObjectNode) response));
    }

    public CompletionStage<Result> getTransactionIDs() {
        return FutureConverters.toJava(ask(marketActor, new MarketActorProtocol.GetTransactionIDs(db), 1000))
                .thenApply(response -> ok((ObjectNode) response));
    }

    public CompletionStage<Result> getTransaction(int transactionID) {
        return FutureConverters.toJava(ask(marketActor, new MarketActorProtocol.GetTransaction(db, transactionID), 1000))
                .thenApply(response -> ok((ObjectNode) response));
    }

    public CompletionStage<Result> getSellOfferIDs() {
        return FutureConverters.toJava(ask(marketActor, new MarketActorProtocol.GetSellOfferIDs(db), 1000))
                .thenApply(response -> ok((ObjectNode) response));
    }

    public CompletionStage<Result> getSellOffer(String offerID) {
        return FutureConverters.toJava(ask(marketActor, new MarketActorProtocol.GetSellOffer(db, offerID), 1000))
                .thenApply(response -> ok((ObjectNode) response));
    }

    public CompletionStage<Result> requestBuyTransaction(double maxRate, int amount) {
        return FutureConverters.toJava(ask(userActor, new UserActorProtocol.RequestBuyTransaction(db, marketActor, maxRate, amount), 1000))
                .thenApply(response -> ok((ObjectNode) response));
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
