package frc.robot.commands.autonomous;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.IntakeCommandGroup;
import frc.robot.commands.IntakeForAutoCommand;
import frc.robot.subsystems.Arm.Arm;
import frc.robot.subsystems.Claw;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Wrist;

public class AutoPickup extends SequentialCommandGroup {


    public AutoPickup(Drivetrain drivetrain, Intake intake, Arm arm, Wrist wrist, Claw claw, double delay) {
        addCommands(
            new ParallelCommandGroup(
                new DriveDuration(drivetrain, 2500.0, delay, null, null),
                new IntakeCommandGroup(wrist, arm),
                new IntakeForAutoCommand(claw)

            )

        );
    }
}
