package galenscovell.soulslite.processing.fsm;


import galenscovell.soulslite.actors.components.StateComponent;

public enum RangedAgent implements State<StateComponent> {

    DEFAULT() {
        @Override
        public void enter(StateComponent stateComponent) {
            System.out.println("Enemy NORMAL");
        }

        @Override
        public void exit(StateComponent stateComponent) {
            System.out.println("Enemy NORMAL");
        }

        @Override
        public void update(StateComponent stateComponent) {

        }

        @Override
        public float getFrameRatio() {
            return 0;
        }
    },

    PURSUE() {
        @Override
        public void enter(StateComponent stateComponent) {
            System.out.println("Enemy PURSUE");
        }

        @Override
        public void exit(StateComponent stateComponent) {
            System.out.println("Enemy PURSUE");
        }

        @Override
        public void update(StateComponent stateComponent) {

        }

        @Override
        public float getFrameRatio() {
            return 0;
        }
    },

    EVADE() {
        @Override
        public void enter(StateComponent stateComponent) {
            System.out.println("Enemy EVADE");
        }

        @Override
        public void exit(StateComponent stateComponent) {
            System.out.println("Enemy EVADE");
        }

        @Override
        public void update(StateComponent stateComponent) {

        }

        @Override
        public float getFrameRatio() {
            return 0;
        }
    },

    ATTACK() {
        Float frames;
        Float currentFrame;

        @Override
        public void enter(StateComponent stateComponent) {
            System.out.println("Enemy ATTACK");
            frames = 16f;
            currentFrame = frames;
        }

        @Override
        public void exit(StateComponent stateComponent) {
            System.out.println("Enemy ATTACK");
        }

        @Override
        public void update(StateComponent stateComponent) {
            currentFrame--;
            if (currentFrame <= 0) {
                if (stateComponent.revertToPreviousState()) {
                    System.out.println("Enemy reverted to " + stateComponent.getCurrentState().toString());
                }
            }
        }

        @Override
        public float getFrameRatio() {
            return currentFrame / frames;
        }
    }
}
