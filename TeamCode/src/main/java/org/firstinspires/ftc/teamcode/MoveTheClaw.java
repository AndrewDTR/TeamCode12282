package org.firstinspires.ftc.teamcode;
// import lines were omitted. OnBotJava will add them automatically.

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class MoveTheClaw extends LinearOpMode {
    DcMotorEx motorLift;

    @Override
    public void runOpMode() {
        motorLift = hardwareMap.get(DcMotorEx.class, "motorLift");

        // Reset the encoder during initialization
        motorLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();

        // Set the motor's target position to 300 ticks
        motorLift.setTargetPosition(-1500);

        // Switch to RUN_TO_POSITION mode
        motorLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Start the motor moving by setting the max velocity to 200 ticks per second
        motorLift.setVelocity(350);

        // While the Op Mode is running, show the motor's status via telemetry
        while (opModeIsActive()) {
            telemetry.addData("velocity", motorLift.getVelocity());
            telemetry.addData("position", motorLift.getCurrentPosition());
            telemetry.addData("is at target", !motorLift.isBusy());
            telemetry.update();
        }
    }
}