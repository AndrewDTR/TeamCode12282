package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name="Motor Test")
public class MotorTest extends LinearOpMode {
    DcMotorEx motorLift;

    @Override
    public void runOpMode() throws InterruptedException {
        motorLift = hardwareMap.get(DcMotorEx.class, "motorLift");
        motorLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLift.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();
        while (opModeIsActive()) {

            if (gamepad1.a) {
                motorLift.setPower(1);
            } else if (gamepad1.b) {
                motorLift.setPower(-0.2);
            } else {
                motorLift.setPower(0);

            }
        }
    }
}
