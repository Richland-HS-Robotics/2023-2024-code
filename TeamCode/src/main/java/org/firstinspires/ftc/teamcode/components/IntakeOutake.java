package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.HelperFunctions;
import org.firstinspires.ftc.teamcode.util.SimplerHardwareMap;

public class IntakeOutake {
    private Claw claw;
    private LinearSlide linearSlide;

    private Controller controller;

    private Telemetry telemetry;

    private StateMachine currentRunningStateMachine;

    public enum PossibleStates {
        DOWN_IDLE,
        UP_IDLE,
        GRAB_PIXEL,
        SCORE_PIXEL,
        RAISE_LIFT,
        LOWER_LIFT
    }

    private PossibleStates state;


    public IntakeOutake(SimplerHardwareMap hardwareMap, Telemetry telemetry,Controller controller){
        this(hardwareMap,telemetry,controller, PossibleStates.DOWN_IDLE);
    }


    public IntakeOutake(SimplerHardwareMap hardwareMap, Telemetry telemetry, Controller controller, PossibleStates state){
        this.claw = new Claw(hardwareMap,telemetry);
        this.linearSlide = new LinearSlide(hardwareMap,telemetry);
        this.telemetry = telemetry;
        this.controller = controller;


        this.state = state;
    }

    /**
     * Set the current state.
     */
    private void setState(PossibleStates state){
        this.state = state;
        switch (state){
            case GRAB_PIXEL:
                currentRunningStateMachine = new GrabPixelStateMachine();
                break;
            case RAISE_LIFT:
                currentRunningStateMachine = new RaiseLiftStateMachine();
                break;
            case LOWER_LIFT:
                currentRunningStateMachine = new LowerLiftStateMachine();
                break;
            case SCORE_PIXEL:
                currentRunningStateMachine = new ScorePixelStateMachine();
                break;
        }
    }


    public void tick(){
        switch (state){
            case DOWN_IDLE:
                handleDownIdle();
                break;
            case UP_IDLE:
                handleUpIdle();
                break;
            case GRAB_PIXEL:
            case LOWER_LIFT:
                currentRunningStateMachine.tick(claw, linearSlide);
                if(currentRunningStateMachine.isDone()){
                    this.setState(PossibleStates.DOWN_IDLE);
                }
                break;
            case SCORE_PIXEL:
            case RAISE_LIFT:
                currentRunningStateMachine.tick(claw, linearSlide);
                if(currentRunningStateMachine.isDone()){
                    this.setState(PossibleStates.UP_IDLE);
                }
                break;
        }
    }

    /**
     * Handle the down idle state.
     */
    private void handleDownIdle(){
        if(controller.clawGrab()){
            this.setState(PossibleStates.GRAB_PIXEL);
            return;
        }

        if(controller.linearSlideInOut() > 0){
            this.setState(PossibleStates.RAISE_LIFT);
        }
    }

    /**
     * Handle the up idle state.
     */
    private void handleUpIdle(){
        if(controller.releasePixels().x || controller.releasePixels().y){
            this.setState(PossibleStates.SCORE_PIXEL);
            return;
        }

        if(controller.linearSlideInOut() < 0){
            this.setState(PossibleStates.LOWER_LIFT);
        }
    }


    private interface StateMachine{
        /**
         * Tick the state machine.
         */
        void tick(Claw claw, LinearSlide linearSlide);
        /**
         * See if the state machine is finished or not.
         * @return True if the machine is done, otherwise false.
         */
        boolean isDone();

    }
    private static class GrabPixelStateMachine implements StateMachine{
        private ElapsedTime timer;
        private enum GrabState {
            CLOSE,
            RAISE,
            OPEN,
            LOWER,
            DONE,
        }

        private static final double CLAW_OPEN_TIME = 0.5;
        private static final double CLAW_ROTATION_ERROR = 0.15;

        GrabState state;
        public GrabPixelStateMachine(){
            this.state = GrabState.CLOSE;
            this.timer = new ElapsedTime();
        }

        @Override
        public void tick(Claw claw, LinearSlide linearSlide){
            switch(state){
                case CLOSE:
                    claw.closeClaw();
                    if(timer.time() > CLAW_OPEN_TIME){
                        state = GrabState.RAISE;
                        timer.reset();
                    }
                    break;
                case RAISE:
                    claw.raiseClaw();
                    if(HelperFunctions.ge_threshold(claw.getCurrentPosition(),Claw.ARM_ROTATION_RANGE,CLAW_ROTATION_ERROR)){
                        state = GrabState.OPEN;
                        timer.reset();
                    }
                    break;
                case OPEN:
                    claw.openClaw();
                    if(timer.time() > CLAW_OPEN_TIME){
                        state = GrabState.LOWER;
                    }
                    break;
                case LOWER:
                    claw.lowerClaw();
                    if(HelperFunctions.le_threshold(claw.getCurrentPosition(),0,CLAW_ROTATION_ERROR)){
                        state = GrabState.DONE;
                    }
                    break;
            }
        }

        @Override
        public boolean isDone(){
            return state == GrabState.DONE;
        }
    }

    // TODO
    private static class ScorePixelStateMachine implements StateMachine{

        @Override
        public void tick(Claw claw, LinearSlide linearSlide) {

        }

        @Override
        public boolean isDone() {
            return false;
        }
    }

    public static class RaiseLiftStateMachine implements StateMachine {
        private enum State{
            RAISE_LIFT,
            RAISE_CLAW,
            DONE
        }
        private State state;

        public RaiseLiftStateMachine(){
            this.state = State.RAISE_LIFT;
        }

        @Override
        public void tick(Claw claw, LinearSlide linearSlide) {
            switch (state){
                case RAISE_LIFT:
                    linearSlide.setSlidePosition(1);
                    if(HelperFunctions.ge_threshold(linearSlide.getSlidePosition(),1,0.01)){
                        state = State.RAISE_CLAW;
                    }
                    break;
                case RAISE_CLAW:
                    claw.raiseClaw();
                    if(HelperFunctions.ge_threshold(claw.getCurrentPosition(),Claw.ARM_ROTATION_RANGE,0.1)){
                        state = State.DONE;
                    }

                    break;
            }
        }

        @Override
        public boolean isDone() {
            return state == State.DONE;
        }
    }

    public static class LowerLiftStateMachine implements StateMachine {
        enum State{
            CLAW_DOWN,
            LIFT_DOWN,
            DONE
        }
        private State state;

        public LowerLiftStateMachine(){
            this.state = State.CLAW_DOWN;
        }

        @Override
        public void tick(Claw claw, LinearSlide linearSlide) {
            switch(state){
                case CLAW_DOWN:
                    claw.lowerClaw();
                    if(HelperFunctions.le_threshold(claw.getCurrentPosition(),0,0.15)){
                        this.state = State.LIFT_DOWN;
                    }
                    break;
                case LIFT_DOWN:
                    linearSlide.setSlidePosition(0);
                    if(HelperFunctions.le_threshold(linearSlide.getSlidePosition(),0,0.01)){
                        this.state = State.DONE;
                    }
                    break;
            }

        }

        @Override
        public boolean isDone() {
            return state == State.DONE;
        }
    }
}
