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
                // make the robot have the same constraints as the physical one yippee
                .setColorScheme(new ColorSchemeRedDark())
                .setConstraints(35, 35, Math.toRadians(120), Math.toRadians(120), 5)
                .followTrajectorySequence(drive ->
                        // Start here
                        drive.trajectorySequenceBuilder(new Pose2d(35, -60, Math.toRadians(90)))
                                .forward(48)
                                .strafeLeft(11.5)
                                .forward(10)
                                .waitSeconds(1)
                                .back(9)
                                .lineToLinearHeading(new Pose2d(50, -12, Math.toRadians(0)))
                                .forward(12)
                                .waitSeconds(1)
                                .lineToLinearHeading(new Pose2d(24, -8, Math.toRadians(90)))
                                .waitSeconds(1)
                                .lineToLinearHeading(new Pose2d(50, -12, Math.toRadians(0)))
                                .forward(12)
                                .waitSeconds(1)
                                .lineToLinearHeading(new Pose2d(24, -8, Math.toRadians(90)))
                                .waitSeconds(1)
                                .lineToLinearHeading(new Pose2d(50, -12, Math.toRadians(0)))
                                .forward(12)
                                .waitSeconds(1)
                                .lineToLinearHeading(new Pose2d(24, -8, Math.toRadians(90)))
                                .waitSeconds(1)


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