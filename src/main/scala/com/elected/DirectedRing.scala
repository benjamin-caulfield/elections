package com.elected

import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.AbstractBehavior
import akka.actor.typed.scaladsl.ActorContext
import akka.actor.typed.scaladsl.Behaviors

object Actor {
  final case class Message(message: String)
}

object InitActor {
  def apply(): Behavior[String] =
    Behaviors.setup(context => new InitActor(context))
}

