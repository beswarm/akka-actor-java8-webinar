/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 * Updated  by Jose Alberto Guastavino
 */
package com.lightbend.akkasample.sample3;

import akka.actor.AbstractActor;
import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.japi.pf.DeciderBuilder;
import scala.concurrent.duration.Duration;

public class Supervisor extends AbstractLoggingActor {

  public static final OneForOneStrategy STRATEGY = new OneForOneStrategy(
    10,
    Duration.create("10 seconds"),
    DeciderBuilder
      .match(RuntimeException.class, ex -> SupervisorStrategy.restart())
      .build()
  );


	@Override
	public Receive createReceive() {
		// user/supervisor/child
		log().info("Supervisor.createReceive()");
		final ActorRef child = getContext().actorOf(NonTrustWorthyChild.props(),"child");
		return receiveBuilder().matchAny(any -> child.forward(any, getContext())).build();
	}

 

  @Override
  public SupervisorStrategy supervisorStrategy() {
    return STRATEGY;
  }

  public static Props props() {
    return Props.create(Supervisor.class);
  }
}
