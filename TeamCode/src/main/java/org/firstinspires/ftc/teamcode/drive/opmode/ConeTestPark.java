package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

/*
 * This is an example of a more complex path to really test the tuning.
 */
@Autonomous(group = "drive")
public class ConeTestPark extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        waitForStart();

        if (isStopRequested()) return;

        drive.setPoseEstimate(new Pose2d(36, -60, Math.toRadians(87.32)));

        Trajectory untitled0 = drive.trajectoryBuilder(new Pose2d(36.00, -60.00, Math.toRadians(87.32)))
                .splineTo(new Vector2d(47.84, -11.68), Math.toRadians(360.00))
                .splineTo(new Vector2d(60.79, 47.84), Math.toRadians(90.00))
                .splineTo(new Vector2d(26.68, 61.74), Math.toRadians(180.00))
                .splineTo(new Vector2d(-34.58, 56.05), Math.toRadians(222.71))
                .build();

        drive.followTrajectory(untitled0);

    }
}
