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
            result.put("amount", getTransaction.amount);
            result.put("rate", getTransaction.rate);
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
}
