package org.firstinspires.ftc.teamcode.drive.opmode;
// import lines were omitted. OnBotJava will add them automatically.

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class Testing extends LinearOpMode {
    DcMotorEx motorLift;

    @Override
    public void runOpMode() {
        motorLift = hardwareMap.get(DcMotorEx.class, "lift");

        // Reset the encoder during initialization
        motorLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();



        motorLift.setTargetPosition(-1500);

        motorLift.setPower(1);


        // Switch to RUN_TO_POSITION mode
        motorLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Start the motor moving by setting the max velocity to 200 ticks per second




        // While the Op Mode is running, show the motor's status via telemetry
        while (opModeIsActive()) {

            if (gamepad1.circle) {
                motorLift.setTargetPosition(-1500);
            }
            if (gamepad1.cross) {
                motorLift.setTargetPosition(0);

            }
            if (gamepad1.triangle) {
                motorLift.setTargetPosition(-2000);
            }
            if (gamepad1.square) {
                motorLift.setTargetPosition(-2500);
            }
            if (gamepad1.share) {
                motorLift.setTargetPosition(-3000);
            }


            telemetry.addData("velocity", motorLift.getVelocity());
            telemetry.addData("position", motorLift.getCurrentPosition());
            telemetry.addData("is at target", !motorLift.isBusy());
            telemetry.update();
        }
    }
}