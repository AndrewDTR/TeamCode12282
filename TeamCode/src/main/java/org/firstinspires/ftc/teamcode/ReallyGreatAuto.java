package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous
public class ReallyGreatAuto extends LinearOpMode {
    @Override


    public void runOpMode() throws InterruptedException {

        Servo tor;
        tor = hardwareMap.servo.get("tor");

        DcMotorEx motorLift;
        motorLift = hardwareMap.get(DcMotorEx.class, "lift");

        motorLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d startPose = new Pose2d(35, -60, Math.toRadians(90));

        drive.setPoseEstimate(startPose);

        TrajectorySequence trajSeq = drive.trajectorySequenceBuilder(startPose)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> tor.setPosition(1))
                //
                // Get to the pole, 1+
                //
                .splineToSplineHeading(new Pose2d(35.00, -20.50, Math.toRadians(90)), Math.toRadians(90.00))
                .splineToSplineHeading(new Pose2d(35.00, -13.50, Math.toRadians(130.00)), Math.toRadians(90.00))
                .UNSTABLE_addTemporalMarkerOffset(-1, () -> motorLift.setTargetPosition(-3000))
                .forward(10)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> tor.setPosition(0.5))
                .waitSeconds(1)
                .back(8)
                .UNSTABLE_addTemporalMarkerOffset(-0.5, () -> motorLift.setTargetPosition(0))
                .turn(Math.toRadians(-130))
                //
                // Go for 1+1
                //
                .forward(26)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> tor.setPosition(1))
                .waitSeconds(1)
                .back(26)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> motorLift.setTargetPosition(-3000))
                .turn(Math.toRadians(130))
                //
                // Score for 1+1
                //
                .forward(8)
                .UNSTABLE_addTemporalMarkerOffset(0.5, () -> tor.setPosition(0.5))
                .waitSeconds(1)
                .back(8)
                .UNSTABLE_addTemporalMarkerOffset(-0.5, () -> motorLift.setTargetPosition(0))
                .turn(Math.toRadians(-130))
                //
                // Go for 1+2
                //
                .forward(26)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> tor.setPosition(1))
                .waitSeconds(1)
                .back(26)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> motorLift.setTargetPosition(-3000))
                .turn(Math.toRadians(130))
                //
                // Score for 1+2
                //
                .forward(9)
                .UNSTABLE_addTemporalMarkerOffset(0.5, () -> tor.setPosition(0.5))
                .waitSeconds(1)
                .back(26)
                .UNSTABLE_addTemporalMarkerOffset(-0.5, () -> motorLift.setTargetPosition(0))
                .build();
        waitForStart();

        if (!isStopRequested())

            motorLift.setPower(1);
        motorLift.setTargetPosition(0);

        tor.setPosition(1);

        // Set motor to RUN_TO_POSITION, after resetting on line 28
        motorLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        drive.followTrajectorySequence(trajSeq);
    }
}