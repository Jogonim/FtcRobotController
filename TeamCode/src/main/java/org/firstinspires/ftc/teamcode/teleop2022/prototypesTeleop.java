package org.firstinspires.ftc.teamcode.teleop2022;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.DMHardwareTest;

@TeleOp(name="prototypesTeleop", group="prototypes")
public class prototypesTeleop extends LinearOpMode {
    //public DMHardwareTest robot = new DMHardwareTest(false);
    public DMHardwareTest robot = new DMHardwareTest( true);
    boolean bk =  true;


    public void slideControl ( DcMotor left /*DcMotor right*/, boolean up, boolean down) {
        int ticksPerRev = 1152;
        double power = 1.0;
        int rev = 50;


        telemetry.addLine(String.format("\nIn SlideControl . Up =%s down =%s ",up,down));
        telemetry.addLine(String.format("\nIn SlideControl encoder position left = %d,",left.getCurrentPosition()));
        telemetry.update();


        int newTargetLeft = left.getCurrentPosition();
        //int newTargetRight = right.getTargetPosition();

        if (up) {
            newTargetLeft = left.getCurrentPosition() + (ticksPerRev * rev);
           // newTargetRight = right.getTargetPosition() + (ticksPerRev * rev);
        }
        else if (down) {
            newTargetLeft = left.getCurrentPosition() - (ticksPerRev * rev);
          //  newTargetRight = right.getTargetPosition() - (ticksPerRev * rev);
        }
        left.setTargetPosition(newTargetLeft);
       // right.setTargetPosition(newTargetRight);

        telemetry.addLine(String.format("\nIn SlideControl motor encoder position left = %d", newTargetLeft));
        telemetry.update();
        left.setPower(power);
        //right.setPower(power);

        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
       // right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
       // sleep(2000);

        while(left.isBusy()) {
            if (up) {
                telemetry.addLine("Slide is active and moving up ");
                telemetry.update();
            } else if (down) {
                telemetry.addLine("Slide is active and moving down ");
                telemetry.update();

            } else {
                telemetry.addLine("Slide is static ");
                telemetry.update();

            }

        }

        left.setPower(0);
       // right.setPower(0);

    }


    @Override
    public void runOpMode() {
        double y;
        double x;
        double rx;
        boolean linear_slide_up;
        boolean linear_slide_down;

        robot.initTeleOpIMU(hardwareMap, bk);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        robot.slidemotorleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //robot.slidemotorright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            y = gamepad1.right_stick_x;
            x = gamepad1.left_stick_x;
            rx = gamepad1.left_stick_y;
            linear_slide_up = gamepad1.dpad_up;
            linear_slide_down = gamepad1.dpad_down;



            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (-y - x + rx) / denominator;
            double backLeftPower = (-y + x + rx) / denominator;
            double frontRightPower = (-y - x - rx) / denominator;
            double backRightPower = (-y + x - rx) / denominator;

/*
            robot.frontLeft.setPower(frontLeftPower/2.0);
            robot.backLeft.setPower(backLeftPower/2.0);
            robot.frontRight.setPower(frontRightPower/2.0);
            robot.backRight.setPower(backRightPower/2.0);

*/


           // slideControl(robot.slidemotorleft, /*robot.slidemotorright*/ linear_slide_up, linear_slide_down);
            int curpos = robot.slidemotorleft.getCurrentPosition();
            int newpos =0;
            int ticksPerRev = 1152;
            double power = 1.0;
            int rev = 5;
            telemetry.addLine(String.format("\nIn SlideControl motor encoder position left = %d", curpos));
            telemetry.update();
            if (linear_slide_up) {
                newpos = curpos + (ticksPerRev * rev);
                // newTargetRight = right.getTargetPosition() + (ticksPerRev * rev);
                robot.slidemotorleft.setTargetPosition(newpos);
                robot.slidemotorleft.setPower(power);
                robot.slidemotorleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            else if (linear_slide_down) {
                newpos = curpos - (ticksPerRev * rev);
                //  newTargetRight = right.getTargetPosition() - (ticksPerRev * rev);
                robot.slidemotorleft.setTargetPosition(newpos);
                robot.slidemotorleft.setPower(power);
                robot.slidemotorleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }


            while (robot.slidemotorleft.isBusy()){
                curpos = robot.slidemotorleft.getCurrentPosition();
                telemetry.addLine(String.format("\n slide modtor encoder position = %d ", curpos));
                telemetry.update();
            }

        }
    }
}
