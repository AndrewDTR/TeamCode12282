package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Servo Test")
public class ServoTest extends LinearOpMode {
    Servo tor;

    @Override
    public void runOpMode() throws InterruptedException {
        tor = hardwareMap.servo.get("tor");

        waitForStart();
        while(opModeIsActive()){

            if(gamepad1.left_bumper){
                tor.setPosition(0.5);
            }
            else if(gamepad1.right_bumper){
                tor.setPosition(1.0);
            }
            idle();
        }
    }
}
