package tfbot_msgs;

public interface servo_cmd extends org.ros.internal.message.Message {
  static final java.lang.String _TYPE = "tfbot_msgs/servo_cmd";
  static final java.lang.String _DEFINITION = "uint32 canal\nfloat32 cmd";
  int getCanal();
  void setCanal(int value);
  float getCmd();
  void setCmd(float value);
}
