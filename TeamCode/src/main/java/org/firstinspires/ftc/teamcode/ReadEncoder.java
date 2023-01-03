package org.firstinspires.ftc.teamcode;
// import lines were omitted. OnBotJava will add them automatically.

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class ReadEncoder extends LinearOpMode {
    DcMotorEx motorLift;

    @Override
    public void runOpMode() {
        motorLift = hardwareMap.get(DcMotorEx.class, "motorLift");
        waitForStart();
        while (opModeIsActive()) {
            telemetry.addData("Encoder value", motorLift.getCurrentPosition());
            telemetry.update();
        }
    }
}