package tfbot_msgs;

public interface gamepad extends org.ros.internal.message.Message {
  static final java.lang.String _TYPE = "tfbot_msgs/gamepad";
  static final java.lang.String _DEFINITION = "uint8 rX\r\nuint8 rY\r\nuint8 lX\r\nuint8 lY\r\nuint8 Arrow\r\nuint8 Letter\r\nuint8 L\r\nuint8 R\r\nuint8 Control";
  byte getRX();
  void setRX(byte value);
  byte getRY();
  void setRY(byte value);
  byte getLX();
  void setLX(byte value);
  byte getLY();
  void setLY(byte value);
  byte getArrow();
  void setArrow(byte value);
  byte getLetter();
  void setLetter(byte value);
  byte getL();
  void setL(byte value);
  byte getR();
  void setR(byte value);
  byte getControl();
  void setControl(byte value);
}
