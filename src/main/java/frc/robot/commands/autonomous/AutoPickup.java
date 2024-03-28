package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.DriveDuration;
import frc.robot.commands.IntakeCommandGroup;
import frc.robot.commands.instant.IntakeClawCommand;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Claw;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Wrist;

public class AutoPickup extends SequentialCommandGroup {

    private Long timeout;

    public AutoPickup(Drivetrain drivetrain, Intake intake, Arm arm, Wrist wrist, Claw claw) {
        addCommands(
            new DriveDuration(drivetrain, 2500.0),
            new IntakeCommandGroup(wrist, arm),
            new IntakeClawCommand(claw, arm, 5.0)
            



        );
    }
}
