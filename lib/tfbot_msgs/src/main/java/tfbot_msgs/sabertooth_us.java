package tfbot_msgs;

public interface sabertooth_us extends org.ros.internal.message.Message {
  static final java.lang.String _TYPE = "tfbot_msgs/sabertooth_us";
  static final java.lang.String _DEFINITION = "string nombre\nuint32 s1_fwd\nuint32 s2_turn";
  java.lang.String getNombre();
  void setNombre(java.lang.String value);
  int getS1Fwd();
  void setS1Fwd(int value);
  int getS2Turn();
  void setS2Turn(int value);
}
