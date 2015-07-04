import akka.actor._
import akka.pattern.ask
import akka.testkit._
import akka.util.Timeout
import org.specs2.mutable._
import org.specs2.time.NoTimeConversions
import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.duration._

/* A tiny class that can be used as a Specs2 'context'. */
abstract class AkkaTestkitSpecs2Support extends TestKit(ActorSystem())
    with After
    with ImplicitSender {
    // make sure we shut down the actor system after all tests have run
    def after = system.shutdown()
}

/* Both Akka and Specs2 add implicit conversions for adding time-related
   methods to Int. Mix in the Specs2 NoTimeConversions trait to avoid a clash. */
class MonitorSpec extends Specification with NoTimeConversions {
    sequential // forces all tests to be run sequentially

    "Monitor" should {
        /* for every case where you would normally use "in", use 
       "in new AkkaTestkitSpecs2Support" to create a new 'context'. */
        "Process all endpoints" in new AkkaTestkitSpecs2Support {
            within(25 second) {
                implicit val timeout = Timeout(25 seconds)

                val pathEndPointsFile =getClass.getResource("/endpoints.txt").getPath
                val actor = system.actorOf(Props(new MonitorActor(pathEndPointsFile)))
                
                val future = actor ? StartMonitorProcessMsg()
                val listHttpResource = Await.result(future, timeout.duration).asInstanceOf[ListBuffer[HttpResource]]
                
                listHttpResource.size mustEqual 4 //currently the endpoints file contains only 4 lines
            }
        }
    }
}