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
                .setConstraints(35, 35, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        // Start here
                        drive.trajectorySequenceBuilder(new Pose2d(35, -60, Math.toRadians(90)))
                                //
                                // Get to the pole, 1+
                                //
                                .splineToSplineHeading(new Pose2d(35.00, -20.50, Math.toRadians(90)), Math.toRadians(90.00))
                                .splineToSplineHeading(new Pose2d(35.00, -13.50, Math.toRadians(130.00)), Math.toRadians(90.00))
                                .forward(10)
                                .waitSeconds(1)
                                .back(8)
                                .turn(Math.toRadians(-130))
                                //
                                // Go for 1+1
                                //
                                .forward(26)
                                .waitSeconds(1)
                                .back(26)
                                .turn(Math.toRadians(130))
                                //
                                // Score for 1+1
                                //
                                .forward(8)
                                .waitSeconds(1)
                                .back(8)
                                .turn(Math.toRadians(-130))
                                //
                                // Go for 1+2
                                //
                                .forward(26)
                                .waitSeconds(1)
                                .back(26)
                                .turn(Math.toRadians(130))
                                //
                                // Score for 1+2
                                //
                                .forward(9)
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