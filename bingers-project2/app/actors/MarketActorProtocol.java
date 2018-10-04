package actors;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.db.*;
import play.libs.Json;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
                // Close connection!
                conn.close();
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
        double totalAmount; // If success
        double totalCostUSD; // If success
        String message; // If fail

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
                        totalAmount = rs.getDouble("totalAmount");
                        totalCostUSD = rs.getDouble("totalCostUSD");
                    }
                }
                if (!status.equals("success") && !status.equals("error")) {
                    status = "transaction ID (" + transactionID + ") not found";
                }
                // Close connection!
                conn.close();
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
                // Close connection!
                conn.close();
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
                // Close connection!
                conn.close();
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
                // Close connection!
                conn.close();
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
                // Close connection!
                conn.close();
            } catch (Exception e) {
                status = "exception";
                errorMessage = e.toString();
            }
        }
    }

    // Create hold
    public static class CreateHold {
        String status;
        String errorMessage;
        int holdID;

        public CreateHold(Database db, String offerID, double amount, double rate) {
            status = "";

            String queryGetSellOffer = "SELECT * FROM orderbook WHERE offerID = '" + offerID + "';";

            try {
                Connection conn = db.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(queryGetSellOffer);
                double newAmount;
                while(rs.next()) {
                    newAmount = rs.getDouble("amount") - amount;
                    System.out.println("New amount: " + newAmount);
                    if (newAmount < 0.0) {
                        throw new Exception("not enough available");
                    } else {
                        // Reduce count in orderbook
                        String updateSellOffer = "UPDATE orderbook SET amount = " + newAmount + " WHERE offerID='" + offerID + "';";
                        PreparedStatement pstmt = conn.prepareStatement(updateSellOffer);
                        pstmt.executeUpdate();
                        // Store hold info
                        String insertHold = "INSERT INTO holds (offerID,amount,rate) VALUES ('" + offerID + "'," + amount + "," + rate + ");";
                        PreparedStatement pstmt2 = conn.prepareStatement(insertHold, Statement.RETURN_GENERATED_KEYS);
                        pstmt2.executeUpdate();
                        ResultSet rs2 = pstmt2.getGeneratedKeys();
                        if (rs2.next()) {
                            holdID = rs2.getInt(1);
                        }
                        status = "success";
                    }
                }
                // Close connection!
                conn.close();
            } catch (Exception e) {
                status = "exception";
                errorMessage = e.toString();
            }
        }
    }

    // Remove hold
    public static class RemoveHold {
        String status;
        String errorMessage;

        public RemoveHold(Database db, int holdID) {
            status = "";

            try {
                Connection conn = db.getConnection();
                Statement stmt = conn.createStatement();

                String queryGetOfferID = "SELECT * FROM holds WHERE id=" + holdID + ";";
                String deleteHold = "DELETE FROM holds WHERE id=" + holdID + ";";

                ResultSet rs = stmt.executeQuery(queryGetOfferID);
                String offerID = "";
                double holdAmount = 0.0;
                while (rs.next()) {
                    offerID = rs.getString("offerID");
                    holdAmount = rs.getDouble("amount");
                }
                // Get current sell offer amount
                String queryOfferIDAmount = "SELECT * FROM orderbook WHERE offerID='" + offerID + "';";
                ResultSet rs2 = stmt.executeQuery(queryOfferIDAmount);
                while (rs2.next()) {
                    double currentAmount = rs2.getDouble("amount");
                    double newAmount = currentAmount + holdAmount;
                    String updateOfferIDAmount = "UPDATE orderbook SET amount = " + newAmount + " WHERE offerID='" + offerID + "';";
                    PreparedStatement pstmt = conn.prepareStatement(updateOfferIDAmount);
                    pstmt.executeUpdate();
                }
                // Delete the hold from the tracker
                PreparedStatement pstmt2 = conn.prepareStatement(deleteHold);
                pstmt2.executeUpdate();
                status = "success";
                // Close connection!
                conn.close();
            } catch (Exception e) {
                status = "exception";
                errorMessage = e.toString();
            }
        }
    }

    // Confirm hold (create transaction)
    public static class ConfirmHold {
        String status;
        String errorMessage;

        public ConfirmHold(Database db, int holdID) {
            status = "";

            String queryDebugConfirmFailState = "SELECT * FROM debug WHERE flag='confirm_fail';";
            String queryDebugConfirmNoResponseState = "SELECT * FROM debug WHERE flag='confirm_no_response';";

            try {
                Connection conn = db.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(queryDebugConfirmFailState);
                while (rs.next()) {
                    // If confirm_fail enabled
                    if (rs.getInt("value") == 1) {
                        status = "error";
                        errorMessage = "debug confirm_fail enabled";
                        conn.close();
                        return;
                    }
                }
                ResultSet rs2 = stmt.executeQuery(queryDebugConfirmNoResponseState);
                while (rs2.next()) {
                    // If confirm_no_response enabled
                    if (rs2.getInt("value") == 1) {
                        status = "confirm_no_response";
                        conn.close();
                        return;
                    }
                }

                String queryGetOfferID = "SELECT * FROM holds WHERE id=" + holdID + ";";
                String deleteHold = "DELETE FROM holds WHERE id=" + holdID + ";";

                ResultSet rs3 = stmt.executeQuery(queryGetOfferID);
                String offerID = "";
                while (rs3.next()) {
                    offerID = rs3.getString("offerID");
                }
                // Remove sell offer if 0 amount
                String queryEmptyOfferID = "SELECT * FROM orderbook WHERE offerID='" + offerID + "';";
                ResultSet rs4 = stmt.executeQuery(queryEmptyOfferID);
                while (rs4.next()) {
                    if (rs4.getDouble("amount") == 0.0) {
                        String deleteOfferID = "DELETE FROM orderbook WHERE offerID='" + offerID + "';";
                        PreparedStatement pstmt = conn.prepareStatement(deleteOfferID);
                        pstmt.executeUpdate();
                    }
                }
                // Delete the hold from the tracker
                PreparedStatement pstmt2 = conn.prepareStatement(deleteHold);
                pstmt2.executeUpdate();
                status = "success";
                // Close connection!
                conn.close();
            } catch (Exception e) {
                status = "exception";
                errorMessage = e.toString();
            }
        }
    }

    // Enable DEBUG confirm_fail
    public static class DebugConfirmFail {
        String status;

        public DebugConfirmFail(Database db) {
            String updateDebugConfirmFail = "UPDATE debug SET value = 1 WHERE flag='confirm_fail';";

            try {
                Connection conn = db.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(updateDebugConfirmFail);
                pstmt.executeUpdate();
                status = "success";
                // Close connection!
                conn.close();
            } catch (Exception e) {
                status = "exception";
            }
        }
    }

    // Enable DEBUG confirm_fail
    public static class DebugConfirmNoResponse {
        String status;

        public DebugConfirmNoResponse(Database db) {
            String updateDebugConfirmNoResponse = "UPDATE debug SET value = 1 WHERE flag='confirm_no_response';";

            try {
                Connection conn = db.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(updateDebugConfirmNoResponse);
                pstmt.executeUpdate();
                status = "success";
                // Close connection!
                conn.close();
            } catch (Exception e) {
                status = "exception";
            }
        }
    }

    // Enable DEBUG confirm_fail
    public static class DebugReset {
        String status;

        public DebugReset(Database db) {
            String updateDebugReset = "UPDATE debug SET value = 0;";

            try {
                Connection conn = db.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(updateDebugReset);
                pstmt.executeUpdate();
                status = "success";
                // Close connection!
                conn.close();
            } catch (Exception e) {
                status = "exception";
            }
        }
    }
}
