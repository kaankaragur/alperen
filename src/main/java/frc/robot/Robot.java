package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
public class Robot extends TimedRobot {

  private VictorSPX leftMotor1 = new VictorSPX(0);
  private VictorSPX leftMotor2 = new VictorSPX(1);
  private VictorSPX rightMotor1 = new VictorSPX(2);
  private VictorSPX rightMotor2 = new VictorSPX(3);

  private Joystick joy1 = new Joystick(0);

  private double startTime;

  @Override
  public void robotInit() {
  }

  @Override
  public void autonomousInit() {
    startTime = Timer.getFPGATimestamp();
  }

  @Override
  public void autonomousPeriodic() {
    double time = Timer.getFPGATimestamp();
    System.out.println(time - startTime);

    if (time - startTime < 3) {
      leftMotor1.set(ControlMode.PercentOutput,0.6);
      leftMotor2.set(ControlMode.PercentOutput,0.6);
      rightMotor1.set(ControlMode.PercentOutput,-0.6);
      rightMotor2.set(ControlMode.PercentOutput,-0.6);
    } else {
      leftMotor1.set(ControlMode.PercentOutput,0);
      leftMotor2.set(ControlMode.PercentOutput,0);
      rightMotor1.set(ControlMode.PercentOutput,0);
      rightMotor2.set(ControlMode.PercentOutput,0);
    }
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    double speed = -joy1.getRawAxis(1) * 0.6;
    double turn = joy1.getRawAxis(4) * 0.3;

    double left = speed + turn;
    double right = speed - turn;

    leftMotor1.set(ControlMode.PercentOutput,left);
    leftMotor2.set(ControlMode.PercentOutput,left);
    rightMotor1.set(ControlMode.PercentOutput,-right);
    rightMotor2.set(ControlMode.PercentOutput,-right);
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}