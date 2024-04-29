package com.elected

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import com.elected.Main.rootBehavior
import com.typesafe.config.{Config, ConfigFactory}

object Main extends App {
  println("Starting application")
  val config = ConfigFactory.load()

  val rootBehavior = Behaviors.setup[RingCommand] { context =>
    println("Starting root behavior")

    val totalActors = 10
    val firstActor = createFirstActor(context)
    firstActor ! CreateNextActor(totalActors, 1)

    Behaviors.receiveMessage {
      case _ => Behaviors.same
    }
  }

  private def createFirstActor(context: ActorContext[RingCommand]): ActorRef[RingCommand] = {
    println("creating first actor")
    context.spawn(RingActor(context, context.self), "RingActor0") // Pass null as the initiator
  }

  val system: ActorSystem[RingCommand] = ActorSystem(rootBehavior, "RingActorSystem", config)
}

