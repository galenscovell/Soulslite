package galenscovell.soulslite.processing.fsm;


public enum PlayerState implements State {

    NORMAL() {
        @Override
        public void enter() {
            System.out.println("Enter NORMAL");
        }

        @Override
        public void exit() {
            System.out.println("Exit NORMAL");
        }

        @Override
        public void update(float deltaTime) {

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
        public void enter() {
            System.out.println("Enter DASH");
            frames = 14f;
            currentFrame = frames;
        }

        @Override
        public void exit() {
            System.out.println("Exit DASH");
        }

        @Override
        public void update(float deltaTime) {
            currentFrame--;
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
        public void enter() {
            System.out.println("Enter ATTACK");
            frames = 16f;
            currentFrame = frames;
        }

        @Override
        public void exit() {
            System.out.println("Exit ATTACK");
        }

        @Override
        public void update(float deltaTime) {
            currentFrame--;
        }

        @Override
        public float getFrameRatio() {
            return currentFrame / frames;
        }
    }
}
