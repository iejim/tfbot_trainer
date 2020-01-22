package tfbot;

public class TFRobot extends TFRobotBase
{
    
    Servo ser = new Servo(3);
    public TFRobot(String ns){
        namespace = ns+"/";
    }

    @Override
    public void RobotInit() 
    {
        // Codigo de inicializacion
        // Se ejecuta una sola vez
    }
    
    @Override
    public void TeleopAuto()
    {
        // Codigo de trabajo autonomo
        // Se ejecuta varias veces (no hay que usar While)
        // Hasta que autonomo = 0
        if (autonomo == true)
        {   
        
                   
        }
    }
    
    @Override
    public void TeleopPeriodic()
    {
        // Codigo de trabajo teleoperado
        // Se ejecuta varias veces hasta que se cierra el programa
        
    }
}
