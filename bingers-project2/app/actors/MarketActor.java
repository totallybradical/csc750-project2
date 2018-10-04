package actors;

import actors.MarketActorProtocol.*;
import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import scala.collection.script.Remove;

public class MarketActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    public MarketActor() {
    }

    public static Props props() {
        return Props.create(MarketActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(GetTransactionIDs.class, this::getTransactionIDs)
                .match(GetTransaction.class, this::getTransaction)
                .match(GetSellOffers.class, this::getSellOffers)
                .match(GetSellOfferIDs.class, this::getSellOfferIDs)
                .match(GetSellOffer.class, this::getSellOffer)
                .match(CreateHold.class, this::createHold)
                .match(RemoveHold.class, this::removeHold)
                .match(ConfirmHold.class, this::confirmHold)
                .match(DebugConfirmFail.class, this::debugConfirmFail)
                .match(DebugConfirmNoResponse.class, this::debugConfirmNoResponse)
                .match(DebugReset.class, this::debugReset)
                .build();
    }

    private void getTransactionIDs(GetTransactionIDs getTransactionIDs) {
        log.info("Getting transactions...");
        System.out.println("Getting transactions...");
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode transactions = mapper.createArrayNode();
        ObjectNode result = Json.newObject();
        result.put("status", getTransactionIDs.status);
        // Error Handling
        if (getTransactionIDs.status.equals("exception")) {
            result.put("errorMessage", getTransactionIDs.errorMessage);
        } else {
            for (int i = 0; i < getTransactionIDs.transactions.size(); i++) {
                transactions.add(getTransactionIDs.transactions.get(i));
            }
            result.putArray("transactions").addAll(transactions);
        }
        sender().tell(result, self());
    }

    private void getTransaction(GetTransaction getTransaction) {
        ObjectNode result = Json.newObject();
        log.info("Getting transaction info...");
        System.out.println("Getting transaction info...");
        result.put("status", getTransaction.status);
        // Error Handling
        if (getTransaction.status.equals("exception")) {
            result.put("errorMessage", getTransaction.errorMessage);
        } else if (getTransaction.status.equals("error")){
            // If failed transaction
            result.put("message", getTransaction.message);
        } else if (getTransaction.status.equals("success")) {
            // If success transaction
            result.put("totalAmount", getTransaction.totalAmount);
            result.put("totalCostUSD", getTransaction.totalCostUSD);
        }
        sender().tell(result, self());
    }

    private void getSellOffers(GetSellOffers getSellOffers) {
        log.info("Getting sell offers...");
        System.out.println("Getting sell offers...");
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode offers = mapper.createArrayNode();
        ObjectNode result = Json.newObject();
        result.put("status", getSellOffers.status);
        // Error Handling
        if (getSellOffers.status.equals("exception")) {
            result.put("errorMessage", getSellOffers.errorMessage);
        } else {
            result.putArray("offers").addAll(getSellOffers.sellOffers);
        }
        sender().tell(result, self());
    }

    private void getSellOfferIDs(GetSellOfferIDs getSellOfferIDs) {
        log.info("Getting sell offer IDs...");
        System.out.println("Getting sell offer IDs...");
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode offers = mapper.createArrayNode();
        ObjectNode result = Json.newObject();
        result.put("status", getSellOfferIDs.status);
        // Error Handling
        if (getSellOfferIDs.status.equals("exception")) {
            result.put("errorMessage", getSellOfferIDs.errorMessage);
        } else {
            for (int i = 0; i < getSellOfferIDs.offerIDs.size(); i++) {
                offers.add(getSellOfferIDs.offerIDs.get(i));
            }
            result.putArray("offers").addAll(offers);
        }
        sender().tell(result, self());
    }

    private void getSellOffer(GetSellOffer getSellOffer) {
        ObjectNode result = Json.newObject();
        log.info("Getting sell offer info...");
        System.out.println("Getting sell offer info...");
        result.put("status", getSellOffer.status);
        // Error Handling
        if (getSellOffer.status.equals("exception")) {
            result.put("errorMessage", getSellOffer.errorMessage);
        } else if (getSellOffer.status.equals("success")) {
            // If success transaction
            result.put("amount", getSellOffer.amount);
            result.put("rate", getSellOffer.rate);
        }
        sender().tell(result, self());
    }

    private void createHold(CreateHold createHold) {
        ObjectNode result = Json.newObject();
        log.info("Creating hold...");
        System.out.println("Creating hold...");
        result.put("status", createHold.status);
        result.put("holdID", createHold.holdID);
        // Error Handling
        if (createHold.status.equals("exception")) {
            result.put("errorMessage", createHold.errorMessage);
        }
        sender().tell(result, self());
    }

    private void removeHold(RemoveHold removeHold) {
        ObjectNode result = Json.newObject();
        log.info("Removing hold...");
        System.out.println("Removing hold...");
        result.put("status", removeHold.status);
        // Error Handling
        if (removeHold.status.equals("exception")) {
            result.put("errorMessage", removeHold.errorMessage);
        }
        sender().tell(result, self());
    }

    private void confirmHold(ConfirmHold confirmHold) {
        ObjectNode result = Json.newObject();
        log.info("Confirming hold...");
        System.out.println("Confirming hold...");
        result.put("status", confirmHold.status);
        if (confirmHold.status.equals("confirm_no_response")) {
            return;
        }
        // Error Handling
        if (confirmHold.status.equals("exception") || confirmHold.status.equals("error")) {
            result.put("errorMessage", confirmHold.errorMessage);
        }
        sender().tell(result, self());
    }

    private void debugConfirmFail(DebugConfirmFail debugConfirmFail) {
        ObjectNode result = Json.newObject();
        log.info("Enabling debug confirm fail...");
        System.out.println("Enabling debug confirm fail...");
        result.put("status", debugConfirmFail.status);
        sender().tell(result, self());
    }

    private void debugConfirmNoResponse(DebugConfirmNoResponse debugConfirmNoResponse) {
        ObjectNode result = Json.newObject();
        log.info("Enabling debug confirm no response...");
        System.out.println("Enabling debug confirm no response...");
        result.put("status", debugConfirmNoResponse.status);
        sender().tell(result, self());
    }

    private void debugReset(DebugReset debugReset) {
        ObjectNode result = Json.newObject();
        log.info("Resetting debug flags to 0...");
        System.out.println("Resetting debug flags to 0...");
        result.put("status", debugReset.status);
        sender().tell(result, self());
    }
}
