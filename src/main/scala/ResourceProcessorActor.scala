import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import scalaj.http._

class ResourceProcessorActor extends Actor {
    def receive = {
        case ProcessHTTPResourceMsg(path) => {
            val url: String = "http://" + path
            val startTime: Long = System.currentTimeMillis
            val response: HttpResponse[String] = Http(url).asString
            val endTime: Long = System.currentTimeMillis

            val httpResource = new HttpResource(path, endTime - startTime)

            sender ! ResponseProcessedMsg(httpResource)
        }
        case _ => println("message not recognized!")
    }
}