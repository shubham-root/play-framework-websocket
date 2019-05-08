package actors;

import akka.actor.*;
import akka.japi.pf.ReceiveBuilder;
import org.slf4j.Logger;
import play.api.libs.json.JsValue;
import play.api.libs.json.Json;



public class MyWebSocketActor extends AbstractActor {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger("controllers.HomeController");

    int val = 0;

    public static Props props(ActorRef out) {
        return Props.create(MyWebSocketActor.class, out);
    }

    private final ActorRef out;

    public MyWebSocketActor(ActorRef out) {
        this.out = out;
    }

    @Override
    public Receive createReceive() {

        return receiveBuilder()
                .match(String.class, message -> out.tell("I received your message: " + message, self()))
                .build();
    }

//    public ReceiveBuilder createJsonReceive() {
//        return receiveBuilder()
//                .match(Json.class, message -> out.tell("Connection returns: "+ message.stringify(), self()));
//    }

}