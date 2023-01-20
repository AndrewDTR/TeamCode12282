package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class Twenty23 extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        // Rising edge detector
        Gamepad currentGamepad1 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();

        // Code setup, initialization
        DcMotor leftFront = hardwareMap.dcMotor.get("leftFront");
        DcMotor leftRear = hardwareMap.dcMotor.get("leftRear");
        DcMotor rightFront = hardwareMap.dcMotor.get("rightFront");
        DcMotor rightRear = hardwareMap.dcMotor.get("rightRear");

        Servo tor;
        tor = hardwareMap.servo.get("tor");

        DcMotorEx motorLift;
        motorLift = hardwareMap.get(DcMotorEx.class, "lift");

        motorLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Front left wheel is reversed
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);

        BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);

        waitForStart();

        // Set lift to max speed, targetted to zero (where it should be already)
        motorLift.setPower(1);
        motorLift.setTargetPosition(0);

        // Set motor to RUN_TO_POSITION, after resetting on line 28
        motorLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            // Rising edge
            previousGamepad1.copy(currentGamepad1);
            currentGamepad1.copy(gamepad1);

            // Field-centric driving
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x * 1.1;
            double rx = gamepad1.right_stick_x;

            double botHeading = -imu.getAngularOrientation().firstAngle;

            double rotX = x * Math.cos(botHeading) - y * Math.sin(botHeading);
            double rotY = x * Math.sin(botHeading) + y * Math.cos(botHeading);

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;

            leftFront.setPower(frontLeftPower);
            leftRear.setPower(backLeftPower);
            rightFront.setPower(frontRightPower);
            rightRear.setPower(backRightPower);

            telemetry.addLine("TeleOp is running!");

            // Lift actions

            // Floor
            if (gamepad1.cross) {
                motorLift.setTargetPosition(0);
            }
            // Low junction
            if (gamepad1.square) {
                motorLift.setTargetPosition(-1300);
            }

            // Middle junction
            if (gamepad1.triangle) {
                motorLift.setTargetPosition(-2100);
            }

            // High junction
            if (gamepad1.circle) {
                motorLift.setTargetPosition(-3000);
            }
            if (currentGamepad1.dpad_up && !previousGamepad1.dpad_up) {
                motorLift.setTargetPosition(motorLift.getTargetPosition()-200);
            }
            if (currentGamepad1.dpad_down && !previousGamepad1.dpad_down) {
                motorLift.setTargetPosition(motorLift.getTargetPosition()+200);
            }

            telemetry.addData("Lift Height", motorLift.getCurrentPosition());
            telemetry.addData("Lift Target", motorLift.getTargetPosition());

            // Servo actions
            if(gamepad1.left_bumper){
                // If the position is higher than, like, 500 ticks, we can do this. If not, no real point
                if(motorLift.getCurrentPosition() < -500) {
                    motorLift.setTargetPosition(motorLift.getTargetPosition() - 400);
                    tor.setPosition(0.5);
                }

                else {
                    tor.setPosition(0.5);
                }


                telemetry.addData("Servo Status", "closed");
            }
            if(gamepad1.right_bumper){
                tor.setPosition(1.0);
                gamepad1.rumble(0.2, 0, 200);  // 200 mSec burst on left motor.
                telemetry.addData("Servo Status", "open");
            }



            telemetry.update();
        }
    }
}