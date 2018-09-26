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

    // Add the given amount to the user’s USD or BTC balance.
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
                    purchaseBreakdown.add(purchase);
                }
            }

            System.out.println("Doing more things");

            // TODO: 3. Check Balance (Sufficient Funds for All Holds?)
            GetBalances getBalances = new GetBalances(db, 1);
            double usdBalance = getBalances.usdBalance;
            System.out.println("Current USD balance: " + usdBalance);

            // 4. Ask MarketActor to HOLD (Reduce in Orderbook)
            // TODO: CODE

            // 5. If Step 4 Success, CONFIRM to MarketActor
            // TODO: CODE

            // 6. Update Balances (Remove USD, Add BTC)
            // TODO: CODE

            status = "success";
        }
    }
}
