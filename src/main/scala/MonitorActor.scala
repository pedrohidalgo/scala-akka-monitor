import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.event.Logging
import scala.collection.mutable.ListBuffer

case class HttpResource(path: String, responseTime: Long)

case class StartMonitorProcessMsg()
case class ProcessHTTPResourceMsg(line: String)
case class ResponseProcessedMsg(httpResource: HttpResource)

class MonitorActor(filename: String) extends Actor {
    private val log = Logging(context.system, this)
    private val listHttpResources = ListBuffer.empty[HttpResource]

    private val sourceFile = scala.io.Source.fromFile(filename)
    private val allLines = sourceFile.getLines.toList
    private val totalLines = allLines.size
    sourceFile.close

    private var mainSender: Option[ActorRef] = None

    def receive = {
        case StartMonitorProcessMsg() => {
            mainSender = Some(sender) // save reference to process invoker

            allLines.foreach { path =>
                context.actorOf(Props[ResourceProcessorActor]) ! ProcessHTTPResourceMsg(path)
            }
        }
        case ResponseProcessedMsg(httpResource) => {
            listHttpResources += httpResource

            if (listHttpResources.size == totalLines) {
                mainSender.map(_ ! listHttpResources.sortBy(_.responseTime)) // provide result to process invoker
            }
        }
        case _ => println("message not recognized!")
    }

}
