package tfbot;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;


public class Talker extends AbstractNodeMain {

    @Override
    public GraphName getDefaultNodeName(){
        return GraphName.of("javabot/talker");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        final Publisher<std_msgs.String> publisher = 
            connectedNode.newPublisher("chatter", std_msgs.String._TYPE);
            
        connectedNode.executeCancellableLoop(new CancellableLoop() {
            private int seqNumber;

            @Override
            protected void setup() {
                seqNumber = 0;
            }

            @Override
            protected void loop() throws InterruptedException {
                std_msgs.String str = publisher.newMessage();
                str.setData("Hey there! "+ seqNumber);
                publisher.publish(str);
                seqNumber++;
                Thread.sleep(1000);
            }

        });
    }
}