package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.util.Timeout;
import com.typesafe.config.Config;
import play.libs.akka.InjectedActorSupport;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static akka.pattern.PatternsCS.pipe;

public class UserParentActor extends AbstractActor {

    public static class Create {
        final String id;

        public Create(String id) {
            this.id = id;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }

}
