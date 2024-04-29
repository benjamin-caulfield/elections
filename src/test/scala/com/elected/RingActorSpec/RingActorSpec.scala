package com.elected.RingActorSpec

import akka.actor.testkit.typed.scaladsl.{ScalaTestWithActorTestKit, TestProbe}
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.ActorRef
import com.elected._
import org.scalatest.wordspec.AnyWordSpecLike
import java.util.UUID
import scala.concurrent.duration._

class RingActorSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike {

  "A RingActor" must {

    "create a ring with 10 actors and elect a leader" in {
      val probe = createTestProbe[RingCommand]()
      val ringActor = spawn(Behaviors.ignore[RingCommand], "RingActorTest1")
      ringActor ! CreateNextActor(10, 1)

      probe.expectMessage(RingComplete)
      Thread.sleep(1000)
      probe.expectMessageType[AnnounceLeader]
    }
  }
}