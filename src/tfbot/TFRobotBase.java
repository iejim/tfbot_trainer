package tfbot;

import java.util.*;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import org.apache.commons.logging.Log;
import org.ros.message.MessageListener;
import org.ros.node.topic.Subscriber;

import tfbot_msgs.*;


public class TFRobotBase extends AbstractNodeMain
{
    
    boolean autonomo = false;

    String namespace;

    GamepadClase Gamepad;
    SabertoothClase Sabertooth;

    Map<Integer,Servo> servos_lista;

    Subscriber<tfbot_msgs.gamepad> sub_pad;
    Publisher<tfbot_msgs.drivetrain> pub_saber;
    Publisher<tfbot_msgs.servo_cmd> pub_servo;
    
    public TFRobotBase()
    {
      Gamepad = new GamepadClase();
      Sabertooth = new SabertoothClase();

      servos_lista = new HashMap<Integer, Servo>();
      namespace = "";
      
    }
    
    // public static void main(String[] args)
    // {
    //     tf = new TFRobotBase();
        
    //     tf.inicializar();
    //     tf.correr();
    // }

    @Override
    public void onStart(ConnectedNode connectedNode) {
    
      sub_pad =  
        connectedNode.newSubscriber(namespace+"gamepad_control", tfbot_msgs.gamepad._TYPE);

      pub_saber = 
            connectedNode.newPublisher(namespace+"comando_drivetrain", tfbot_msgs.drivetrain._TYPE);

      pub_servo = 
            connectedNode.newPublisher(namespace+"comando_servos", tfbot_msgs.servo_cmd._TYPE);
    
      inicializar();

      sub_pad.addMessageListener(new MessageListener<tfbot_msgs.gamepad>(){
        
        @Override
        public void onNewMessage(tfbot_msgs.gamepad msg) {
            Gamepad.actualizar(msg);
            
        }
        });


      connectedNode.executeCancellableLoop(new CancellableLoop() {
              private int seqNumber;
  
              @Override
              protected void setup() {
                  seqNumber = 0;
              }
  
              @Override
              protected void loop() throws InterruptedException {
                  
                correr();
                Thread.sleep(5); //ms
              }
  
      

      });
    }

    public void set_namespace(String ns)
    {
      namespace = ns+"/";
    }
    
    
    public void inicializar()
    {
        RobotInit();
    }
    
    public void correr()
    {
      if(autonomo)
      {
          TeleopAuto();
      } else {
          TeleopPeriodic();
      }

      procesar_todo();
    }
    
    public void procesar_todo()
    { 
      // Envia mensajes y etc
      Sabertooth.procesar();
      Gamepad.procesar();

      Iterator<Integer> it = servos_lista.keySet().iterator();
      while (it.hasNext()) {
        int key = it.next();
        servos_lista.get(key).procesar();
      }

    }

    // Para ser sobreescritas
    public void RobotInit()
    {
        return;
    }
    
    public void TeleopAuto()
    {
        return;
    }
    
    public void TeleopPeriodic()
    {
        return;
    }


    @Override
    public GraphName getDefaultNodeName(){
        return GraphName.of("tfbot/tfbot_node");
    }

    public class SabertoothClase
    {
        double cmd_der;
        double cmd_izq;
        boolean procesado = false;

        void izq(double val)
        { 
            cmd_izq = val; //Para que no parezca que no pasa nada
            procesado = false;
        }
        void der(double val)
        {
            cmd_der = val;
            procesado = false;
        }
        void avanzar(double val_izquierda, double val_derecha)
        {
            cmd_izq = val_izquierda;
            cmd_der = val_derecha;

            procesado = false;
        }

        public void procesar()
        {
          if (procesado)
            return;
          
          //Crear mensaje
          tfbot_msgs.drivetrain msg = pub_saber.newMessage();
          
          msg.setNombre("ST1");
          msg.setCmd1Izq((float)cmd_izq);
          msg.setCmd2Der((float)cmd_der);

          //Enviar mensaje
          pub_saber.publish(msg);

          //Post-ajuste
          procesado = true;
          cmd_izq = 0.0;
          cmd_der = 0.0;
        }
    }
    
    public class Servo
    {
        int p;
        double comando;
        boolean procesado = false;

        Servo(int puerto)
        {
            p = puerto;
            comando = 0.0;

            servos_lista.put(servos_lista.size()+1, this);

        }
        
        void pos(double val) 
        {
            comando = val;
            procesado = false;
        }

        void procesar()
        {
          // Crear mensaje (hay que hacerle un casting)
          tfbot_msgs.servo_cmd msg = pub_servo.newMessage();
          
          msg.setCanal(p);
          msg.setCmd((float)comando);

          //Enviar mensaje
          pub_servo.publish(msg);

          // Post-ajuste
          procesado = true;
          comando = 0.0;
          
        }
    }
    
    public class GamepadClase
    {
        boolean actualizado = false;
        boolean leido = false;
        boolean lock = false; // tal vez sea necesario

        int default_cero = 0;
        int default_centro = 128;

        Map<String, Integer> vals;

        GamepadClase()
        {
          vals = new HashMap<String, Integer>();

          vals.put("x", 0);
          vals.put("y", 0);
          vals.put("a", 0);
          vals.put("b", 0);

          vals.put("_lx", 128);
          vals.put("_ly", 128);
          vals.put("_rx", 128);
          vals.put("_ry", 128);

          vals.put("l1", 0);
          vals.put("l2", 0);
          vals.put("l3", 0);
          
          vals.put("r1", 0);
          vals.put("r2", 0);
          vals.put("r3", 0);

        }

        void actualizar(tfbot_msgs.gamepad msg)
        {

          // Extraer valores
          // Cambiar adecuadamente en gamepad.java
          
          //De letras
          leer_letras(msg);

          leer_LR("L", msg);
          leer_LR("R", msg);

          leer_ejes(msg);
          

          actualizado = true;
        }

        void leer_letras(tfbot_msgs.gamepad msg)
        {
          char val = (char)msg.getLetter();
          String letra = String.valueOf(val);
          
          if (letra == "N")
          {
            return; //?
          }

          vals.put(letra.toLowerCase(), 1);

          actualizado = true;
        }

        void leer_LR(String num, tfbot_msgs.gamepad msg)
        {
          
          Integer msg_i;

          if (num == "R")
          {
            msg_i = (int)msg.getR();
          } else {
            msg_i = (int)msg.getL();
          }
        
          if (msg_i > 0 && msg_i < 4)
          {
            vals.put(num.toLowerCase()+ msg_i.toString(), 1);
          } else if (msg_i > 3){
            vals.put(num.toLowerCase()+"2", msg_i);
          }

          actualizado = true;
        }

        void leer_ejes(tfbot_msgs.gamepad msg)
        {
        
          vals.put("_lx", (int)msg.getLX());
          vals.put("_ly", (int)msg.getLY());
          vals.put("_rx", (int)msg.getRX());
          vals.put("_ry", (int)msg.getRY());
          
          actualizado = true;
        }
        void procesar()
        {
          // if (!actualizado)
          //   return;

          Iterator<String> it = vals.keySet().iterator();

          while (it.hasNext()) {
            String key = it.next();
            if (key.length() == 3)
            {
              vals.put(key, default_centro);
            } else {
              vals.put(key,default_cero);
            }
          }

          actualizado = false;
          leido = false;
          
        } 


        int leer(String campo)
        {
          // if (!actualizado)
          //   return campo.length() == 3? default_centro : default_cero;
          
          leido = true;
          int v = vals.get(campo);
          return v;
        }

        int X()
        {
            return leer("x");
        }
        int Y()
        {
            return leer("y");
        }
        int A()
        {
            return leer("a");
        }
        int B()
        {
            return leer("b");
        }
        int lX()
        {
            return leer("_lx");
        }
        int lY()
        {
            return leer("_ly");
        }
        int rX()
        {
            return leer("_rx");
        }
        int rY()
        {
            return leer("_ry");
        }
        int L2()
        {
            return leer("l2");
        }
        int R2()
        {
            return leer("r2");
        }

        int L1()
        {
            return leer("l1");
        }

        int L3()
        {
          return leer("l3");
        }

        int R1()
        {
            return leer("r1");
        }

        int R3()
        {
          return leer("r3");
        }
    }
}