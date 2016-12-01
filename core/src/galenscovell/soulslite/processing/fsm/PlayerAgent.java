package galenscovell.soulslite.processing.fsm;


import galenscovell.soulslite.actors.components.StateComponent;

public enum PlayerAgent implements State<StateComponent> {

    DEFAULT() {
        @Override
        public void enter(StateComponent stateComponent) {
            System.out.println("Player NORMAL");
        }

        @Override
        public void exit(StateComponent stateComponent) {
            System.out.println("Player NORMAL");
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
            System.out.println("Player DASH");
            frames = 14f;
            currentFrame = frames;
        }

        @Override
        public void exit(StateComponent stateComponent) {
            System.out.println("Player DASH");
        }

        @Override
        public void update(StateComponent stateComponent) {
            currentFrame--;
            if (currentFrame <= 0) {
                if (stateComponent.revertToPreviousState()) {
                    System.out.println("Player reverted to " + stateComponent.getCurrentState().toString());
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
            System.out.println("Player ATTACK");
            frames = 16f;
            currentFrame = frames;
        }

        @Override
        public void exit(StateComponent stateComponent) {
            System.out.println("Player ATTACK");
        }

        @Override
        public void update(StateComponent stateComponent) {
            currentFrame--;
            if (currentFrame <= 0) {
                if (stateComponent.revertToPreviousState()) {
                    System.out.println("Player reverted to " + stateComponent.getCurrentState().toString());
                }
            }
        }

        @Override
        public float getFrameRatio() {
            return currentFrame / frames;
        }
    }
}
