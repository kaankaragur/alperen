package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.cameraserver.CameraServer;


public class Robot extends TimedRobot {

  private double motorspeed = 0.2;

  private VictorSPX rightMotor1 = new VictorSPX(2); //sağ
  private VictorSPX leftMotor1 = new VictorSPX(17); //sol
  private Timer m_timer = new Timer();
  private Joystick joy1 = new Joystick(0);

  private boolean busyControl = false;
  private int lastSide;

  Thread BrakeThread;
  Thread VisionThread;
  @Override
  public void robotInit() {
    /* 
    VisionThread = new Thread(
      () -> {
        UsbCamera camera = CameraServer.startAutomaticCapture();
        // Set the resolution
        camera.setResolution(640, 480);
      });
    */
    CameraServer.startAutomaticCapture();
  }

  @Override
  public void autonomousInit() {
    m_timer.start();
  }

  @Override
  public void autonomousPeriodic() {
    double time = m_timer.get();
    //System.out.println(time - startTime);

    if (time < 3) {
      leftMotor1.set(ControlMode.PercentOutput,0.6);
      rightMotor1.set(ControlMode.PercentOutput,-0.6);
      } 
      else {
      leftMotor1.set(ControlMode.PercentOutput,0);
      rightMotor1.set(ControlMode.PercentOutput,0);
    }
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {

    //Brake kontrol ve uygulama
    if(joy1.getRawButtonPressed(0)){ //Button kodu değişecek !!!!!!
      if(lastSide != 0){
        busyControl = true;
        if(BrakeThread.isAlive() == false){
          BrakeThread = new Thread(
            () -> {
            if(lastSide == 1){
              leftMotor1.set(ControlMode.PercentOutput,-motorspeed);
              rightMotor1.set(ControlMode.PercentOutput,-motorspeed);
            }
            else if(lastSide == -1){
              leftMotor1.set(ControlMode.PercentOutput,motorspeed);
              rightMotor1.set(ControlMode.PercentOutput,motorspeed);
            }

            try {
              Thread.sleep(50);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }

            leftMotor1.set(ControlMode.PercentOutput,0);
            rightMotor1.set(ControlMode.PercentOutput,0);

            try {
              Thread.sleep(50);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }

            busyControl = true;
            lastSide = 0;
          });
        }
      }
    }


    //Brake yoksa
    if(busyControl == false){
      double speed = -joy1.getRawAxis(1) * motorspeed;
      double turn = -joy1.getRawAxis(4) * 0.3;

      double left = speed + turn;
      double right = speed - turn;

      leftMotor1.set(ControlMode.PercentOutput,-left);
      rightMotor1.set(ControlMode.PercentOutput,right);

      if(-left > 0 && right > 0){
        lastSide = 1;
      }
      else if(-left < 0 && right < 0){
        lastSide = -1;
      }
      else{
        lastSide = 0;
      }

    }
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
