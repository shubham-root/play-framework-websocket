package actors;

import akka.actor.*;
import akka.japi.pf.ReceiveBuilder;
import org.slf4j.Logger;
import play.api.libs.json.JsValue;
import play.api.libs.json.Json;



public class MyWebSocketActor extends AbstractActor {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger("controllers.HomeController");
    public static int openedConnection =0;
    public static int closedConnection = 0;
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
                .match(String.class, message -> {
                   // System.out.println("I received your message: " + message);
                    out.tell("I received your message: " + message, self());
                })
                .build();
    }

    @Override
    public void preStart(){
        openedConnection++;
        System.out.print(openedConnection + ", ");
    }
    @Override
    public void postStop(){
        closedConnection++;
        System.out.println("Closed" + closedConnection);
    }
//    public ReceiveBuilder createJsonReceive() {
//        return receiveBuilder()
//                .match(Json.class, message -> out.tell("Connection returns: "+ message.stringify(), self()));
//    }

}