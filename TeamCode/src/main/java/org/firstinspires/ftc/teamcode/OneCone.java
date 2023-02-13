/*
 * Copyright (c) 2021 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;

@Autonomous
public class OneCone extends LinearOpMode
{
    // Setting variables and tags, etc.
    OpenCvCamera camera;
    public org.firstinspires.ftc.teamcode.AprilTagDetectionPipeline aprilTagDetectionPipeline;

    static final double FEET_PER_METER = 3.28084;



    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.166;

    // Tag ID 1,2,3 from the 36h11 family
    int LEFT = 1;
    int MIDDLE = 2;
    int RIGHT = 3;

    AprilTagDetection tagOfInterest = null;

    @Override
    public void runOpMode()
    {

        Servo tor;
        tor = hardwareMap.servo.get("tor");

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        // Left of the field
        Pose2d startPose = new Pose2d(60, -36, Math.toRadians(180));

        // Right before cone
        Pose2d driveToCone = new Pose2d(-20, -22, Math.toRadians(135));

        Pose2d derp = new Pose2d(35, -60, Math.toRadians(90));

        drive.setPoseEstimate(derp);

        Trajectory forward = drive.trajectoryBuilder(startPose)

                .forward(28)
                .build();

        Trajectory left = drive.trajectoryBuilder(forward.end())
                .strafeLeft(23)
                .build();

        Trajectory right = drive.trajectoryBuilder(forward.end())
                .strafeRight(23)
                .build();

        DcMotorEx motorLift;
        motorLift = hardwareMap.get(DcMotorEx.class, "lift");

        motorLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorLift.setPower(1);
        motorLift.setTargetPosition(0);

        // Set motor to RUN_TO_POSITION, after resetting on line 28
        motorLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        TrajectorySequence toJunc = drive.trajectorySequenceBuilder(new Pose2d(35, -60, Math.toRadians(90)))
                .lineToLinearHeading(new Pose2d(35, -12, Math.toRadians(135)))
                .UNSTABLE_addTemporalMarkerOffset(-2.5, () -> {motorLift.setTargetPosition(-3000);})
                .forward(9.15)
                .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {tor.setPosition(0.5);})
                .waitSeconds(1)
                .back(9.15)
                .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {motorLift.setTargetPosition(-400);})
                .lineToLinearHeading(new Pose2d(59, -11.5, Math.toRadians(0)))
                .UNSTABLE_addTemporalMarkerOffset(0.25, () -> {tor.setPosition(1);})
                .waitSeconds(1)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {motorLift.setTargetPosition(-3000);})
                .lineToLinearHeading(new Pose2d(35, -12, Math.toRadians(135)))
                .forward(9.32)
                .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {tor.setPosition(0.5);})
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {motorLift.setTargetPosition(0);})
                .waitSeconds(1)
                .back(9.5)
                .build();

        TrajectorySequence toRight = drive.trajectorySequenceBuilder(toJunc.end())
                .turn(Math.toRadians(-45))
                .strafeRight(23)
                .build();

        TrajectorySequence toLeft = drive.trajectorySequenceBuilder(toJunc.end())
                .turn(Math.toRadians(-45))
                .strafeLeft(23)
                .build();

        TrajectorySequence toJustRotate = drive.trajectorySequenceBuilder(toJunc.end())
                .turn(Math.toRadians(-45))

                .build();

        tor.setPosition(1);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new org.firstinspires.ftc.teamcode.AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });

        telemetry.setMsTransmissionInterval(50);

        /*
         * The INIT-loop:
         * This REPLACES waitForStart!
         */
        while (!isStarted() && !isStopRequested())
        {
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if(currentDetections.size() != 0)
            {
                boolean tagFound = false;

                for(AprilTagDetection tag : currentDetections)
                {
                    if(tag.id == LEFT || tag.id == MIDDLE || tag.id == RIGHT)
                    {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }
                }

                if(tagFound)
                {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                }
                else
                {
                    telemetry.addLine("Don't see tag of interest :(");

                    if(tagOfInterest == null)
                    {
                        telemetry.addLine("(The tag has never been seen)");
                    }
                    else
                    {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }

            }
            else
            {
                telemetry.addLine("Don't see tag of interest :(");

                if(tagOfInterest == null)
                {
                    telemetry.addLine("(The tag has never been seen)");
                }
                else
                {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }

            }

            telemetry.update();
            sleep(20);
        }

        /*
         * The START command just came in: now work off the latest snapshot acquired
         * during the init loop.
         */

        /* Update the telemetry */
        if(tagOfInterest != null)
        {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        }
        else
        {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
        }

        /* Actually do something useful */
//        TODO: This
        if(tagOfInterest == null || tagOfInterest.id == LEFT){
            drive.followTrajectorySequence(toJunc);
            drive.followTrajectorySequence(toLeft);
        }else if(tagOfInterest.id == MIDDLE){
            drive.followTrajectorySequence(toJunc);
            drive.followTrajectorySequence(toJustRotate);
        }else{
            drive.followTrajectorySequence(toRight);


        }


        /* You wouldn't have this in your autonomous, this is just to prevent the sample from ending */
        while (opModeIsActive()) {sleep(20);}
    }

    void tagToTelemetry(AprilTagDetection detection)
    {
        telemetry.setDisplayFormat(Telemetry.DisplayFormat.HTML);
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        if(tagOfInterest == null || tagOfInterest.id == LEFT){
            telemetry.addLine("<p style=color:rgb(0,255,0);>'      .'</p>");
            telemetry.addLine("<p style=color:rgb(0,255,0);>'  .;;............'</p>");
            telemetry.addLine("<p style=color:rgb(0,255,0);>'.;;;;::::::::::::'</p>");
            telemetry.addLine("<p style=color:rgb(0,255,0);>' ':;;::::::::::::'</p>");
            telemetry.addLine("<p style=color:rgb(0,255,0);>'   ':'</p>");
        }else if(tagOfInterest.id == MIDDLE){
            telemetry.addLine("<p style=color:rgb(0,255,0);>'     .'</p>");
            telemetry.addLine("<p style=color:rgb(0,255,0);>'   .:;:.'</p>");
            telemetry.addLine("<p style=color:rgb(0,255,0);>' .:;;;;;:.'</p>");
            telemetry.addLine("<p style=color:rgb(0,255,0);>'   ;;;;;'</p>");
            telemetry.addLine("<p style=color:rgb(0,255,0);>'   ;;;;;'</p>");
            telemetry.addLine("<p style=color:rgb(0,255,0);>'   ;;;;;'</p>");
            telemetry.addLine("<p style=color:rgb(0,255,0);>'   ;;;;;'</p>");
            telemetry.addLine("<p style=color:rgb(0,255,0);>'   ;;;;;'</p>");
        }else if (tagOfInterest.id == RIGHT) {
            telemetry.addLine("<p style=color:rgb(0,255,0);>'          .'</p>");
            telemetry.addLine("<p style=color:rgb(0,255,0);>'..........;;.'</p>");
            telemetry.addLine("<p style=color:rgb(0,255,0);>'::::::::::;;;;.'</p>");
            telemetry.addLine("<p style=color:rgb(0,255,0);>'::::::::::;;:''</p>");
            telemetry.addLine("<p style=color:rgb(0,255,0);>'          :''</p>");
        } else {
            telemetry.addLine("<p style=color:rgb(0,255,0);>'  _____  '</p>");
            telemetry.addLine("<p style=color:rgb(0,255,0);>' / ___ \\ '</p>");
            telemetry.addLine("<p style=color:rgb(0,255,0);>'( (   ) )'</p>");
            telemetry.addLine("<p style=color:rgb(0,255,0);>' \\/  / / '</p>");
            telemetry.addLine("<p style=color:rgb(0,255,0);>'    ( (  '</p>");
            telemetry.addLine("<p style=color:rgb(0,255,0);>'    | |  '</p>");
            telemetry.addLine("<p style=color:rgb(0,255,0);>'    (_)  '</p>");
            telemetry.addLine("<p style=color:rgb(0,255,0);>'     _   '</p>");
            telemetry.addLine("<p style=color:rgb(0,255,0);>'    (_)  '</p>");
        }
    }
}