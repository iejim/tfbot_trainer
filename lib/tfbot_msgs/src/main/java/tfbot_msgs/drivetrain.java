package tfbot_msgs;

public interface drivetrain extends org.ros.internal.message.Message {
  static final java.lang.String _TYPE = "tfbot_msgs/drivetrain";
  static final java.lang.String _DEFINITION = "string nombre\nfloat32 cmd1_izq\nfloat32 cmd2_der";
  java.lang.String getNombre();
  void setNombre(java.lang.String value);
  float getCmd1Izq();
  void setCmd1Izq(float value);
  float getCmd2Der();
  void setCmd2Der(float value);
}
