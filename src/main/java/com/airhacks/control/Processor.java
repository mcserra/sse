package com.airhacks.control;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import org.reactivestreams.Publisher;

@ApplicationScoped
public class Processor {

    private FlowableEmitter<String> emitter;
    private Flowable<String> flowable;

    @PostConstruct
    public void init() {
        this.flowable = Flowable.create(e -> this.emitter = e, BackpressureStrategy.LATEST);
    }

    @Produces
    @ApplicationScoped
    @FlowableProcessor
    public Publisher<String> flowable() {
        return flowable;
    }

    @Produces
    @ApplicationScoped
    @FlowableProcessor
    public FlowableEmitter<String> emitter() {
        return emitter;
    }

}
