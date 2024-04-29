package com.elected

import java.util.UUID

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.AbstractBehavior
import akka.actor.typed.scaladsl.ActorContext
import akka.actor.typed.scaladsl.Behaviors


sealed trait RingCommand
case class CreateNextActor(totalActors: Int, actorsSoFar: Int) extends RingCommand
case object RingComplete extends RingCommand
case class StartElection(id: UUID) extends RingCommand
case class CompareCurrentLeader(leaderID: UUID) extends RingCommand
case class AnnounceLeader(leaderID: UUID) extends RingCommand

object RingActor {
  def apply(context: ActorContext[RingCommand], initiator: ActorRef[RingCommand]): Behavior[RingCommand] =
    Behaviors.setup(context => new RingActor(context, initiator))
}

class RingActor(context: ActorContext[RingCommand], initiator: ActorRef[RingCommand]) extends AbstractBehavior[RingCommand](context) {
  var nextActor: ActorRef[RingCommand] = _
  val id = UUID.randomUUID()
  var totalActors = 0

  override def onMessage(msg: RingCommand): Behavior[RingCommand] = {
    msg match {
      case CreateNextActor(totalActors, actorsSoFar) =>
        println(s"Creating actor $actorsSoFar out of $totalActors")

        if (actorsSoFar < totalActors) {
          context.log.info(s"Creating actor $actorsSoFar out of $totalActors")
          nextActor = context.spawn(RingActor.apply(context, initiator), s"RingActor$actorsSoFar")
          nextActor ! CreateNextActor(totalActors, actorsSoFar + 1)
        } else {
          nextActor = initiator
          initiator ! RingComplete
          context.log.info("Ring complete")
        }
        this
      case RingComplete =>
        initiator ! StartElection(UUID.randomUUID())
        context.log.info("Starting election")
        this
      case StartElection(id) =>
        nextActor ! CompareCurrentLeader(id)
        this
      case CompareCurrentLeader(leaderID) =>
        if (nextActor == initiator) {
          initiator ! AnnounceLeader(id)
        } else if (leaderID.compareTo(id) < 0) {
          nextActor ! CompareCurrentLeader(id)
        } else {
          nextActor ! CompareCurrentLeader(leaderID)
        }
        this
      case AnnounceLeader(leaderID) =>
        if (nextActor == initiator) {
        } else {
          nextActor ! AnnounceLeader(leaderID)
        }
        this
    }
  }
}


