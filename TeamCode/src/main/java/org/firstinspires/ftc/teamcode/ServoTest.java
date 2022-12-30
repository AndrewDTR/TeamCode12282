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

            if(gamepad1.a){
                tor.setPosition(1.0);
            }
            else if(gamepad1.b){
                tor.setPosition(0.5);
            }
            idle();
        }
    }
}
