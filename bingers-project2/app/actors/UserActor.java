package actors;

import actors.UserActorProtocol.*;
import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

public class UserActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    final int userID;
    final double usdBalance;
    final double btcBalance;

    public UserActor(int userID, double usdBalance, double btcBalance) {
        this.userID = userID;
        this.usdBalance = usdBalance;
        this.btcBalance = btcBalance;
    }

    public static Props props(int userID, double usdBalance, double btcBalance) {
        return Props.create(UserActor.class, userID, usdBalance, btcBalance);
    }

    @Override
    public void preStart() {
        log.info("UserActor {} started with USD: {} and BTC: {}", userID, usdBalance, btcBalance);
    }

    @Override
    public void postStop() {
        log.info("UserActor {} stopped with USD: {} and BTC: {}", userID, usdBalance, btcBalance);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(AddBalanceUSD.class, this::addBalanceUSD)
                .match(ReduceBalanceUSD.class, this::reduceBalanceUSD)
                .match(AddBalanceBTC.class, this::addBalanceBTC)
                .match(GetBalances.class, this::getBalances)
                .match(RequestBuyTransaction.class, this::requestBuyTransaction)
                .match(AddEventToLog.class, this::addEventToLog)
                .build();
    }

    private void addBalanceUSD(AddBalanceUSD addBalanceUSD) {
        log.info("Adding to balance...");
        System.out.println("Adding to balance...");
        ObjectNode result = Json.newObject();
        result.put("status", addBalanceUSD.status);
        // Error Handling
        if (addBalanceUSD.status.equals("exception")) {
            result.put("errorMessage", addBalanceUSD.errorMessage);
        }
        sender().tell(result, self());
    }

    private void reduceBalanceUSD(ReduceBalanceUSD reduceBalanceUSD) {
        log.info("Reducing balance...");
        System.out.println("Reducing balance...");
        ObjectNode result = Json.newObject();
        result.put("status", reduceBalanceUSD.status);
        // Error Handling
        if (reduceBalanceUSD.status.equals("exception")) {
            result.put("errorMessage", reduceBalanceUSD.errorMessage);
        }
        sender().tell(result, self());
    }

    private void addBalanceBTC(AddBalanceBTC addBalanceBTC) {
        log.info("Adding to balance...");
        System.out.println("Adding to balance...");
        ObjectNode result = Json.newObject();
        result.put("status", addBalanceBTC.status);
        // Error Handling
        if (addBalanceBTC.status.equals("exception")) {
            result.put("errorMessage", addBalanceBTC.errorMessage);
        }
        sender().tell(result, self());
    }

    private void getBalances(GetBalances getBalances) {
        log.info("Balance requested...");
        System.out.println("Balance requested...");
        ObjectNode result = Json.newObject();
        result.put("status", getBalances.status);
        // Error Handling
        if (getBalances.status.equals("exception")) {
            result.put("errorMessage", getBalances.errorMessage);
        } else {
            result.put("usd", getBalances.usdBalance);
            result.put("btc", getBalances.btcBalance);
        }
        sender().tell(result, self());
    }

    private void requestBuyTransaction(RequestBuyTransaction requestBuyTransaction) {
        log.info("Requesting buy transaction...");
        System.out.println("Requesting buy transaction...");
        ObjectNode result = Json.newObject();
        result.put("status", requestBuyTransaction.status);
        // Error Handling
        if (requestBuyTransaction.status.equals("exception")) {
            result.put("errorMessage", requestBuyTransaction.errorMessage);
        } else if (requestBuyTransaction.status.equals("error")) {
            result.put("errorMessage", requestBuyTransaction.errorMessage);
        } else {
            // result.put("offers", requestBuyTransaction.sellOffers);
            result.put("transactionID", requestBuyTransaction.transactionID);
        }
        sender().tell(result, self());
    }

    private void addEventToLog(AddEventToLog addEventToLog) {
        ObjectNode result = Json.newObject();
        result.put("status", addEventToLog.status);
        sender().tell(result, self());
    }
}
