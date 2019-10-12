package com.airhacks.boundary;

import com.airhacks.control.FlowableProcessor;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import org.reactivestreams.Publisher;

@Path("ping")
public class PingResource {

    private SseBroadcaster broadcaster;

    @Inject
    @FlowableProcessor
    private Publisher<String> publisher;

    @Inject
    @FlowableProcessor
    FlowableEmitter<String> emitter;

    @Context
    private void setSse(Sse sse) {
        this.broadcaster = sse.newBroadcaster();
        OutboundSseEvent.Builder eventBuilder = sse.newEventBuilder()
                .name("message")
                .mediaType(MediaType.APPLICATION_JSON_TYPE);
        Flowable.fromPublisher(publisher)
                .doOnNext(data -> System.out.println("Sending event: " + data))
                .map(data -> eventBuilder.data(data).build())
                .doOnError(e -> System.out.println(e.getMessage()))
                .doFinally(this.broadcaster::close)
                .subscribe(this.broadcaster::broadcast);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response ping() {
        return Response.ok(new Ping("message")).build();
    }

    @GET
    @Path("event")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void getStockPrices(@Context SseEventSink sseEventSink, @Context Sse sse) {
        try (SseEventSink sink = sseEventSink) {
            sink.send(sse.newEvent("data"));
            sink.send(sse.newEvent("MyEventName", "more data"));

            OutboundSseEvent event = sse.newEventBuilder().
                    id("EventId").
                    name("EventName").
                    data("Data").
                    reconnectDelay(10000).
                    comment("Anything i wanna comment here!").
                    build();

            sink.send(event);
        }
    }

    @GET
    @Path("subscribe")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void listen(@Context SseEventSink sseEventSink) {
        broadcaster.register(sseEventSink);
    }

    @GET
    @Path("publish")
    public Response broadcast(@QueryParam("message") String data, @Context Sse sse) {
        this.emitter.onNext(data);
        return Response.ok().build();
    }
}
