import akka.actor.Actor._
import akka.actor._
import scala.collection.mutable.ListBuffer

object Sample extends App {

    import akka.util.Timeout
    import scala.concurrent.duration._
    import scala.concurrent.Await
    import akka.pattern.ask
    import akka.dispatch.ExecutionContexts._

    implicit val ec = global

    override def main(args: Array[String]) {
        val system = ActorSystem("System")

        val filename =getClass.getResource("/endpoints.txt").getPath
        val actor = system.actorOf(Props(new MonitorActor(filename)))

        implicit val timeout = Timeout(25 seconds)
        val startTime = System.currentTimeMillis

        val future = actor ? StartMonitorProcessMsg()
        val listHttpResource = Await.result(future, timeout.duration).asInstanceOf[ListBuffer[HttpResource]]

        val endTime = System.currentTimeMillis
        val totalTime = endTime - startTime

        println("total time http requests ddd ff: " + totalTime + " milliseconds")

        listHttpResource
            .sortBy(_.responseTime)
            .foreach(httpResource => println(httpResource.path + "," + httpResource.responseTime))

        system.shutdown
    }
}
