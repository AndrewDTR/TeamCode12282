package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
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

                .splineToSplineHeading(new Pose2d(35.00, -30.50, Math.toRadians(90)), Math.toRadians(90.00))
                .UNSTABLE_addTemporalMarkerOffset(-0.5, () -> motorLift.setTargetPosition(-3000))
                .splineToSplineHeading(new Pose2d(35.00, -12.50, Math.toRadians(130.00)), Math.toRadians(90.00))
                .forward(10)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> tor.setPosition(0.5))
                .waitSeconds(0.5)
                // Return
                .back(10)
                .UNSTABLE_addTemporalMarkerOffset(-0.5, () -> motorLift.setTargetPosition(0))
                // Go to cone stack
                .lineToLinearHeading(new Pose2d(55, -12, Math.toRadians(0)))
                .forward(5)
                .waitSeconds(0.5)
                .back(5)
                .lineToLinearHeading(new Pose2d(35.00, -12.50, Math.toRadians(130.00)))
                .forward(10)
                .waitSeconds(1)
                .back(10)
                .lineToLinearHeading(new Pose2d(55, -12, Math.toRadians(0)))
                .forward(5)
                .waitSeconds(0.5)
                .back(5)
                .lineToLinearHeading(new Pose2d(35.00, -12.50, Math.toRadians(130.00)))
                .forward(10)
                .waitSeconds(1)
                .back(8)
                .lineToLinearHeading(new Pose2d(12.00, -10.50, Math.toRadians(90.00)))
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