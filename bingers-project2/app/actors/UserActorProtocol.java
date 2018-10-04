package actors;

import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.db.*;
import play.libs.Json;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;

public class UserActorProtocol {

    // Add the given amount to the user’s USD balance.
    public static class AddBalanceUSD {
        String status;
        String errorMessage;
        double usdBalance;

        public AddBalanceUSD(Database db, int userID, double amount) {
            String queryGetUSDBalance = "SELECT balance FROM accounts WHERE userID = " + userID + " AND accountType = 'usd';";

            try {
                Connection conn = db.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(queryGetUSDBalance);
                while(rs.next()) {
                    usdBalance = rs.getDouble("balance");
                }
                // Add amount to balance
                usdBalance += amount;
                // Update in DB
                String updateAddUSDBalance = "UPDATE accounts SET balance = " + usdBalance + " WHERE userID = " + userID + " AND accountType = 'usd';";
                PreparedStatement pstmt = conn.prepareStatement(updateAddUSDBalance);
                pstmt.executeUpdate();
                // Report success
                status = "success";
                // Close connection!
                conn.close();
            } catch (Exception e) {
                status = "exception";
                errorMessage = e.toString();
            }
        }
    }

    // Reduce the user’s USD balance by the given amount
    public static class ReduceBalanceUSD {
        String status;
        String errorMessage;
        double usdBalance;

        public ReduceBalanceUSD(Database db, int userID, double amount) {
            String queryGetUSDBalance = "SELECT balance FROM accounts WHERE userID = " + userID + " AND accountType = 'usd';";

            try {
                Connection conn = db.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(queryGetUSDBalance);
                while(rs.next()) {
                    usdBalance = rs.getDouble("balance");
                }
                // Add amount to balance
                usdBalance -= amount;
                // Update in DB
                String updateReduceUSDBalance = "UPDATE accounts SET balance = " + usdBalance + " WHERE userID = " + userID + " AND accountType = 'usd';";
                PreparedStatement pstmt = conn.prepareStatement(updateReduceUSDBalance);
                pstmt.executeUpdate();
                // Report success
                status = "success";
                // Close connection!
                conn.close();
            } catch (Exception e) {
                status = "exception";
                errorMessage = e.toString();
            }
        }
    }

    // Add the given amount to the user’s BTC balance.
    public static class AddBalanceBTC {
        String status;
        String errorMessage;
        double btcBalance;

        public AddBalanceBTC(Database db, int userID, double amount) {
            String queryGetUSDBalance = "SELECT balance FROM accounts WHERE userID = " + userID + " AND accountType = 'btc';";

            try {
                Connection conn = db.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(queryGetUSDBalance);
                while(rs.next()) {
                    btcBalance = rs.getDouble("balance");
                }
                // Add amount to balance
                btcBalance += amount;
                // Update in DB
                String updateAddUSDBalance = "UPDATE accounts SET balance = " + btcBalance + " WHERE userID = " + userID + " AND accountType = 'btc';";
                PreparedStatement pstmt = conn.prepareStatement(updateAddUSDBalance);
                pstmt.executeUpdate();
                // Report success
                status = "success";
                // Close connection!
                conn.close();
            } catch (Exception e) {
                status = "exception";
                errorMessage = e.toString();
            }
        }
    }

    // Return the user’s USD and BTC balance.
    public static class GetBalances {
        String status;
        String errorMessage;
        double usdBalance;
        double btcBalance;

        public GetBalances(Database db, int userID) {
            String queryGetUSDBalance = "SELECT balance FROM accounts WHERE userID = " + userID + " AND accountType = 'usd';";
            String queryGetBTCBalance = "SELECT balance FROM accounts WHERE userID = " + userID + " AND accountType = 'btc';";

            try {
                Connection conn = db.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(queryGetUSDBalance);
                while(rs.next()) {
                    usdBalance = rs.getDouble("balance");
                }
                rs = stmt.executeQuery(queryGetBTCBalance);
                while(rs.next()) {
                    btcBalance = rs.getDouble("balance");
                }
                // Report success
                status = "success";
                // Close connection!
                conn.close();
            } catch (Exception e) {
                status = "exception";
                errorMessage = e.toString();
            }
        }
    }

    // Request a buy transaction for the given BTC amount at the given maximum rate.
    public static class RequestBuyTransaction {
        String status;
        String errorMessage;

        JsonNode sellOffers; // Array of offers (with data)
        List<ObjectNode> purchaseBreakdown;

        public RequestBuyTransaction(Database db, ActorRef marketActor, double maxRate, double amount) {
            // TODO: 1. Get Sell Offers Available
            Future<Object> getSellOffers = ask(marketActor, new MarketActorProtocol.GetSellOffers(db, maxRate), 1000);
            Duration waitTime = Duration.create(1, TimeUnit.SECONDS);
            try {
                ObjectNode getSellOffersResponse = (ObjectNode) Await.result(getSellOffers, waitTime);
                sellOffers = getSellOffersResponse.get("offers");
            } catch (Exception e) {
                status = "exception";
                System.out.println(e.toString());
            }

            // TODO: 2. Verify Enough BTC Exists (Below Rate)
            double totalAvailableBelowMaxRate = 0.0;
            for (int i = 0; i < sellOffers.size(); i++) {
                totalAvailableBelowMaxRate += sellOffers.get(i).get("amount").asDouble();
            }
            // Return if not enough BTC available
            if (amount > totalAvailableBelowMaxRate) {
                status = "error";
                errorMessage = "Not enough BTC available";
                return;
            }

            // TODO: 3. Determine Optimal Buy (Least Expensive, Split Holds as Needed)
            double tempAmount = amount;
            purchaseBreakdown = new ArrayList<>();
            for (int i = 0; i < sellOffers.size(); i++) {
                if (tempAmount > 0.0) {
                    ObjectNode purchase = Json.newObject();
                    purchase.put("offerID", sellOffers.get(i).get("offerID").asText());
                    if (tempAmount <= sellOffers.get(i).get("amount").asDouble()) {
                        purchase.put("amount", tempAmount);
                        tempAmount = 0.0;
                    } else {
                        purchase.put("amount", sellOffers.get(i).get("amount").asDouble());
                        tempAmount -= sellOffers.get(i).get("amount").asDouble();
                    }
                    purchase.put("rate", sellOffers.get(i).get("rate").asDouble());
                    purchaseBreakdown.add(purchase);
                }
            }

            // TODO: 4. Check Balance (Sufficient Funds for All Holds?)
            GetBalances getBalances = new GetBalances(db, 1);
            double usdBalance = getBalances.usdBalance;
            double totalCost = 0.0;
            for (int i = 0; i < purchaseBreakdown.size(); i++) {
                totalCost += (purchaseBreakdown.get(i).get("amount").asDouble() * purchaseBreakdown.get(i).get("rate").asDouble());
            }
            // Return if insufficient funds
            if (totalCost > usdBalance) {
                status = "error";
                errorMessage = "Insufficient funds";
                return;
            }

            // TODO: 5. Ask MarketActor to HOLD (Reduce orderbook and Track in holds)
            // The actor sends a Hold request to the MarketActor, with the OfferID and the number to hold
            for (int i = 0; i < purchaseBreakdown.size(); i++) {
                String thisOfferID = purchaseBreakdown.get(i).get("offerID").asText();
                double thisAmount = purchaseBreakdown.get(i).get("amount").asDouble();
                double thisRate = purchaseBreakdown.get(i).get("rate").asDouble();
                System.out.println("Request made to hold " + thisAmount + " BTC from offerID " + thisOfferID);
                Future<Object> holdRequest = ask(marketActor, new MarketActorProtocol.CreateHold(db, thisOfferID, thisAmount, thisRate), 1000);
                try {
                    ObjectNode holdRequestResponse = (ObjectNode) Await.result(holdRequest, waitTime);
                    if (holdRequestResponse.get("status").asText().equals("success")) {
                        // TODO: 6. If Step 5 Success, CONFIRM to MarketActor
                        // Confirm sent for EACH HOLD
                        int holdID = holdRequestResponse.get("holdID").asInt();
                        System.out.println("Successful hold with ID: " + holdID);
                        Future<Object> confirmHoldRequest = ask(marketActor, new MarketActorProtocol.ConfirmHold(db, holdID), 1000);
                        try {
                            ObjectNode confirmHoldRequestResponse = (ObjectNode) Await.result(confirmHoldRequest, waitTime);
                            if (confirmHoldRequestResponse.get("status").asText().equals("success")) {
                                double purchaseCostUSD = thisAmount * thisRate;
                                // Reduce user's USD balance by cost of purchase
                                ReduceBalanceUSD reducedBalanceUSD = new ReduceBalanceUSD(db, 1, purchaseCostUSD);
                                // Add BTC to user's BTC balance
                                AddBalanceBTC addBalanceBTC = new AddBalanceBTC(db, 1, thisAmount);
                            }
                        } catch (Exception e) {
                            status = "exception";
                            System.out.println("You're here... also: " + e.toString());
                        }
                    }
                } catch (Exception e) {
                    status = "exception";
                    System.out.println(e.toString());
                }
            }
            status = "success";
        }
    }
}
