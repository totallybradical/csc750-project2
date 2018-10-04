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
        // String clear_eventslog_table = "DELETE FROM eventslog;"; // Not clearing per TA comment on Piazza
        String clear_orderbook_table = "DELETE FROM orderbook;";
        String clear_transactions_table = "DELETE FROM transactions;";
        String clear_users_table = "DELETE FROM users;";
        String clear_accounts_table = "DELETE FROM accounts;";
        String clear_holds_table = "DELETE FROM holds;";
        String clear_debug_table = "DELETE FROM debug;";
        String reset_counters = "DELETE FROM sqlite_sequence WHERE name='eventslog' OR name='orderbook' OR name='transactions' OR name='users' OR name='accounts' OR name='holds' OR name='debug';";
        String initialize_users = "INSERT INTO users (name, email) VALUES ('Bradford Ingersoll', 'bingers@ncsu.edu');";
        String initialize_accounts = "INSERT INTO accounts (userID, accountType, balance) VALUES (1, 'usd', 0.0), (1, 'btc', 0.0);";
        String initialize_orderbook = "INSERT INTO orderbook (rate, amount, offerID) VALUES (100.00, 5.0, '431671cb'), (80.00, 2.0, '16b961ed'), (50.00, 12.0, '1e06381d');";
        String initialize_debug = "INSERT INTO debug (flag) VALUES ('confirm_fail'), ('confirm_no_response');";
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
            // Clear debug table
            stmt.execute(clear_debug_table);
            // Reset counters
            stmt.execute(reset_counters);
            // Initialize orderbook table
            stmt.execute(initialize_orderbook);
            // Initialize users table
            stmt.execute(initialize_users);
            // Initialize accounts table
            stmt.execute(initialize_accounts);
            // Initialize debug table
            stmt.execute(initialize_debug);

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
        return FutureConverters.toJava(ask(userActor, new UserActorProtocol.RequestBuyTransaction(db, marketActor, maxRate, amount), 5000))
                .thenApply(response -> ok((ObjectNode) response));
    }

    // ======= DEBUG FUNCTIONS =======

    public CompletionStage<Result> debugConfirmFail() {
        return FutureConverters.toJava(ask(marketActor, new MarketActorProtocol.DebugConfirmFail(db), 1000))
                .thenApply(response -> ok((ObjectNode) response));
    }

    public CompletionStage<Result> debugConfirmNoResponse() {
        return FutureConverters.toJava(ask(marketActor, new MarketActorProtocol.DebugConfirmNoResponse(db), 1000))
                .thenApply(response -> ok((ObjectNode) response));
    }

    public CompletionStage<Result> debugReset() {
        return FutureConverters.toJava(ask(marketActor, new MarketActorProtocol.DebugReset(db), 1000))
                .thenApply(response -> ok((ObjectNode) response));
    }
}
