package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);


        RoadRunnerBotEntity mySecondBot = new DefaultBotBuilder(meepMeep)
                // We set this bot to be red
                .setColorScheme(new ColorSchemeRedDark())
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        // Start here
                        drive.trajectorySequenceBuilder(new Pose2d(35, -60, Math.toRadians(90)))

                                // Line up, go 1
                                .splineToSplineHeading(new Pose2d(35.00, -20.50, Math.toRadians(90)), Math.toRadians(90.00))
                                .splineToSplineHeading(new Pose2d(35.00, -12.50, Math.toRadians(130.00)), Math.toRadians(90.00))
                                .forward(10)
                                .waitSeconds(1)
                                // Return
                                .back(10)
                                // Go to cone stack
                                .lineToLinearHeading(new Pose2d(50, -12, Math.toRadians(90)))
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
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_KAI_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                // Add both of our declared bot entities
                .addEntity(mySecondBot)
                .start();
    }
}