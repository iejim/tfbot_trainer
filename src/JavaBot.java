import java.net.URI;
import java.net.URISyntaxException;

import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

// import tfbot.Listener;
// import tfbot.Talker;
import tfbot.TFRobot;

public class JavaBot {

    // private static RosCore rosCore;
    private static NodeMainExecutor executor = DefaultNodeMainExecutor.newDefault();
    private static URI master;
    private static String namespace = "bb_tf_04";
    private static String masterhost = "bb-tf-04.local";
    private static String localhost;

    public static void main(String args[]) {
        /*
         * System.out.println("Iniciando Ros"); rosCore = RosCore.newPublic(11311);
         * rosCore.start();
         * 
         * try { rosCore.awaitStart(); } catch (Exception e) { throw new
         * RuntimeException(e); }
         */
        // if (System.getenv("COMPUTERNAME")!= "")
        // { 
        //     localhost = System.getenv("COMPUTERNAME")+".local";
        // } else {
            localhost = "nitocris.local";
        // }
        
        try {
            master = new URI("http://"+masterhost+":11311/");
            
        }
        catch (URISyntaxException e) {
            System.out.println("Mala URI para ROSMASTER");
        }

        execute("TFRobot", new TFRobot(namespace));
        // execute("Listener", new Listener());

    }

   

    private static void execute(String name, NodeMain node) {
        // URI master;

        System.out.println("Starting " + name + " node...");
        NodeConfiguration config = NodeConfiguration.newPublic(localhost);  //No conviene localhost porque confunde a los otros hosts
        // config.setMasterUri(rosCore.getUri());

        config.setMasterUri(master);
        
        config.setNodeName(name);
        executor.execute(node, config);
    }
}