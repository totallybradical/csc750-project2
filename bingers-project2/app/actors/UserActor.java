package actors;

import akka.actor.*;

public class UserActor extends AbstractActor {

    public static Props getProps() {
        return Props.create(UserActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
//                .match(GetFlights.class, getFlights -> {
//                    ObjectNode result = Json.newObject();
//                    result.put("status", "success");
//                    result.putArray("flights").addAll(getFlights.flights);
//                    sender().tell(result, self());
//                })
//                .match(GetFlight.class, getFlight -> {
//                    ObjectNode result = Json.newObject();
//                    result.put("status", "success");
//                    result.put("seats", getFlight.seats_avail);
//                    sender().tell(result, self());
//                })
//                .match(GetStatus.class, getStatus -> {
//                    ObjectNode result = Json.newObject();
//                    result.put("status", "success");
//                    result.put("confirmFail", this.confirmFail);
//                    result.put("confirmNoResponse", this.confirmNoResponse);
//                    sender().tell(result, self());
//                })
//                .match(ConfirmFail.class, confirmFail -> {
//                    this.confirmFail = true;
//                    ObjectNode result = Json.newObject();
//                    result.put("status", "success");
//                    sender().tell(result, self());
//                })
//                .match(ConfirmNoResponse.class, confirmNoResponse -> {
//                    this.confirmNoResponse = true;
//                    ObjectNode result = Json.newObject();
//                    result.put("status", "success");
//                    sender().tell(result, self());
//                })
//                .match(Reset.class, Reset -> {
//                    this.confirmFail = false;
//                    this.confirmNoResponse = false;
//                    ObjectNode result = Json.newObject();
//                    result.put("status", "success");
//                    sender().tell(result, self());
//                })
                .build();
    }
}
