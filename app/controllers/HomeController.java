package controllers;

import actors.MyWebSocketActor;
import actors.UserParentActor;
import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.webjars.play.WebJarsUtil;
import play.libs.F.Either;
import play.libs.streams.ActorFlow;
import play.mvc.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static akka.pattern.PatternsCS.ask;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@Singleton
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    private final Duration t = Duration.of(1, ChronoUnit.SECONDS);
    private final Logger logger = org.slf4j.LoggerFactory.getLogger("controllers.HomeController");

    private final ActorSystem actorSystem;
    private final Materializer materializer;

    @Inject
    public HomeController(ActorSystem actorSystem, Materializer materializer) {
        this.actorSystem = actorSystem;
        this.materializer = materializer;
    }

        public Result index() {
        return ok("Success");
    }

//    public WebSocket webSocket() {
//        logger.info("reached inside controller");
//        return WebSocket.Json.acceptOrResult(request -> {
//            if (sameOriginCheck(request)) {
//                final CompletionStage<Flow<JsonNode, JsonNode, NotUsed>> future = wsFutureFlow(request);
//                final CompletionStage<Either<Result, Flow<JsonNode, JsonNode, ?>>> stage = future.thenApply(Either::Right);
//                return stage.exceptionally(this::logException);
//            } else {
//                return forbiddenResult();
//            }
//        });
//    }

//    private CompletionStage<Flow<JsonNode, JsonNode, NotUsed>> wsFutureFlow(Http.RequestHeader request) {
//        long id = request.asScala().id();
//        MyWebSocketActor.
//
//        return ask(userParentActor, create, t).thenApply((Object flow) -> {
//            final Flow<JsonNode, JsonNode, NotUsed> f = (Flow<JsonNode, JsonNode, NotUsed>) flow;
//            return f.named("websocket");
//        });
//    }

    int val = 0;

    public WebSocket webSocket() {
        return WebSocket.Text.accept(
                request -> ActorFlow.actorRef(MyWebSocketActor::props, actorSystem, materializer));
    }

    private CompletionStage<Either<Result, Flow<JsonNode, JsonNode, ?>>> forbiddenResult() {
        final Result forbidden = Results.forbidden("forbidden");
        final Either<Result, Flow<JsonNode, JsonNode, ?>> left = Either.Left(forbidden);

        return CompletableFuture.completedFuture(left);
    }

    private void onStop(MyWebSocketActor actor) throws  Exception{

        actor.postStop();

    }

    private Either<Result, Flow<JsonNode, JsonNode, ?>> logException(Throwable throwable) {
        logger.error("Cannot create websocket", throwable);
        Result result = Results.internalServerError("error");
        return Either.Left(result);
    }

    private boolean sameOriginCheck(Http.RequestHeader rh) {
        final Optional<String> origin = rh.header("Origin");

        if (! origin.isPresent()) {
            logger.error("originCheck: rejecting request because no Origin header found");
            return false;
        } else if (originMatches(origin.get())) {
            logger.debug("originCheck: originValue = " + origin);
            return true;
        } else {
            logger.error("originCheck: rejecting request because Origin header value " + origin + " is not in the same origin: "
                    + String.join(", ", validOrigins));
            return false;
        }
    }

    private List<String> validOrigins = Arrays.asList("localhost:9000", "localhost:19001", "*", "chrome-extension://fgponpodhbmadfljofbimhhlengambbn");
    private boolean originMatches(String actualOrigin) {
        return validOrigins.stream().anyMatch(actualOrigin::contains);
    }


}
