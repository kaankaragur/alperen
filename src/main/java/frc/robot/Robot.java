package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.cameraserver.CameraServer;


public class Robot extends TimedRobot {

  private double motorspeed = 0.6;
  private VictorSPX rightMotor1 = new WPI_VictorSPX(2); //sağ
  private VictorSPX leftMotor1 = new WPI_VictorSPX(17); //sol
  private Timer m_timer = new Timer();
  private Joystick joy1 = new Joystick(0);

  private int lastSide = 0;
  private int mode = 0;
  private boolean busyControl = false;

  Thread BrakeThread = new Thread(
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
      Thread.sleep(300);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    leftMotor1.set(ControlMode.PercentOutput,0);
    rightMotor1.set(ControlMode.PercentOutput,0);

    try {
      Thread.sleep(300);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    lastSide = 0;
    busyControl = false;
  });

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
      leftMotor1.set(ControlMode.PercentOutput,motorspeed);
      rightMotor1.set(ControlMode.PercentOutput,motorspeed);
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
    
    if(joy1.getRawButtonPressed(5) && BrakeThread.isAlive() == false){ //Button kodu değişecek !!!!!!
      if(lastSide != 0){
        if(BrakeThread.isAlive() == false){
          busyControl = true;
          BrakeThread.start();
        }
      }
    }
    

    if(joy1.getRawButton(3)){
      if(mode == 0){
        mode = 1;
      }
      else {
        mode = 0;
      }
      System.out.println("mode=" + String.valueOf(mode));
    }


    //Brake yoksa
    if(mode == 0 && busyControl == false){
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
    else if(busyControl == false){
      double right = joy1.getRawAxis(1) * motorspeed;
      double left = joy1.getRawAxis(5) * motorspeed;

      leftMotor1.set(ControlMode.PercentOutput,left);
      rightMotor1.set(ControlMode.PercentOutput,-right);

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
