package actors;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.db.*;
import play.libs.Json;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarketActorProtocol {

    // Get a list of successful transactions
    public static class GetTransactionIDs {
        String status;
        String errorMessage;
        List<Integer> transactions;


        public GetTransactionIDs(Database db) {
            transactions = new ArrayList<>();
            String queryGetTransactions = "SELECT id FROM transactions;";

            try {
                Connection conn = db.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(queryGetTransactions);
                while(rs.next()) {
                    transactions.add(rs.getInt("id"));
                }
                // Report success
                status = "success";
            } catch (Exception e) {
                status = "exception";
                errorMessage = e.toString();
            }
        }
    }

    // Get details of a given transaction
    public static class GetTransaction {
        String status;
        String errorMessage;
        double amount; // If success transaction
        double rate; // If success transaction
        String message; // If failed transaction

        public GetTransaction(Database db, int transactionID) {
            status = "";
            String queryGetTransaction = "SELECT * FROM transactions WHERE id = " + transactionID + ";";

            try {
                Connection conn = db.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(queryGetTransaction);
                while(rs.next()) {
                    // If failed transaction
                    if (rs.getString("status").equals("error")) {
                        status = "error";
                        message = rs.getString("message");
                    } else {
                        // Report success
                        status = "success";
                        amount = rs.getDouble("amount");
                        rate = rs.getDouble("rate");
                    }
                }
                if (!status.equals("success") && !status.equals("error")) {
                    status = "transaction ID (" + transactionID + ") not found";
                }
            } catch (Exception e) {
                status = "exception";
                errorMessage = e.toString();
            }
        }
    }

    // Get a list of sell offers (full data)
    public static class GetSellOffers {
        String status;
        String errorMessage;
        List<ObjectNode> sellOffers;


        // Get ALL sell offers
        public GetSellOffers(Database db) {
            sellOffers = new ArrayList<>();
            String queryGetTransactions = "SELECT * FROM orderbook ORDER BY rate;";

            try {
                Connection conn = db.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(queryGetTransactions);
                while(rs.next()) {
                    ObjectNode sellOffer = Json.newObject();
                    sellOffer.put("offerID", rs.getString("offerID"));
                    sellOffer.put("rate", rs.getString("rate"));
                    sellOffer.put("amount", rs.getString("amount"));
                    sellOffers.add(sellOffer);
                }
                // Report success
                status = "success";
            } catch (Exception e) {
                status = "exception";
                errorMessage = e.toString();
            }
        }

        // Get sell offers below rate
        public GetSellOffers(Database db, double maxRate) {
            sellOffers = new ArrayList<>();
            String queryGetTransactions = "SELECT * FROM orderbook WHERE rate <= " + maxRate + " ORDER BY rate;";

            try {
                Connection conn = db.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(queryGetTransactions);
                while(rs.next()) {
                    ObjectNode sellOffer = Json.newObject();
                    sellOffer.put("offerID", rs.getString("offerID"));
                    sellOffer.put("rate", rs.getString("rate"));
                    sellOffer.put("amount", rs.getString("amount"));
                    sellOffers.add(sellOffer);
                }
                // Report success
                status = "success";
            } catch (Exception e) {
                status = "exception";
                errorMessage = e.toString();
            }
        }
    }

    // Get a list of sell Offer IDs
    public static class GetSellOfferIDs {
        String status;
        String errorMessage;
        List<String> offerIDs;


        public GetSellOfferIDs(Database db) {
            offerIDs = new ArrayList<>();
            String queryGetTransactions = "SELECT offerID FROM orderbook;";

            try {
                Connection conn = db.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(queryGetTransactions);
                while(rs.next()) {
                    offerIDs.add(rs.getString("offerID"));
                }
                // Report success
                status = "success";
            } catch (Exception e) {
                status = "exception";
                errorMessage = e.toString();
            }
        }
    }

    // Get sell offer details
    public static class GetSellOffer {
        String status;
        String errorMessage;
        double amount; // If success transaction
        double rate; // If success transaction

        public GetSellOffer(Database db, String offerID) {
            status = "";
            String queryGetSellOffer = "SELECT * FROM orderbook WHERE offerID = '" + offerID + "';";

            try {
                Connection conn = db.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(queryGetSellOffer);
                while(rs.next()) {
                    // Report success
                    status = "success";
                    amount = rs.getDouble("amount");
                    rate = rs.getDouble("rate");
                }
                if (!status.equals("success") && !status.equals("error")) {
                    status = "sell offer ID (" + offerID + ") not found";
                }
            } catch (Exception e) {
                status = "exception";
                errorMessage = e.toString();
            }
        }
    }
}
