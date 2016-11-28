package galenscovell.soulslite.processing.fsm;


import galenscovell.soulslite.actors.components.StateComponent;

public enum PlayerState implements State<StateComponent> {

    NORMAL() {
        @Override
        public void enter(StateComponent stateComponent) {
            System.out.println("Enter NORMAL");
        }

        @Override
        public void exit(StateComponent stateComponent) {
            System.out.println("Exit NORMAL");
        }

        @Override
        public void update(StateComponent stateComponent) {

        }

        @Override
        public float getFrameRatio() {
            return 0;
        }
    },

    DASH() {
        Float frames;
        Float currentFrame;

        @Override
        public void enter(StateComponent stateComponent) {
            System.out.println("Enter DASH");
            frames = 14f;
            currentFrame = frames;
        }

        @Override
        public void exit(StateComponent stateComponent) {
            System.out.println("Exit DASH");
        }

        @Override
        public void update(StateComponent stateComponent) {
            currentFrame--;
            if (currentFrame <= 0) {
                if (stateComponent.revertToPreviousState()) {
                    System.out.println("Reverted to " + stateComponent.getCurrentState().toString());
                }
            }
        }

        @Override
        public float getFrameRatio() {
            return currentFrame / frames;
        }
    },

    ATTACK() {
        Float frames;
        Float currentFrame;

        @Override
        public void enter(StateComponent stateComponent) {
            System.out.println("Enter ATTACK");
            frames = 16f;
            currentFrame = frames;
        }

        @Override
        public void exit(StateComponent stateComponent) {
            System.out.println("Exit ATTACK");
        }

        @Override
        public void update(StateComponent stateComponent) {
            currentFrame--;
            if (currentFrame <= 0) {
                if (stateComponent.revertToPreviousState()) {
                    System.out.println("Reverted to " + stateComponent.getCurrentState().toString());
                }
            }
        }

        @Override
        public float getFrameRatio() {
            return currentFrame / frames;
        }
    }
}
