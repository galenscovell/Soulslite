package galenscovell.soulslite.processing.fsm;


import galenscovell.soulslite.actors.components.AgentStateComponent;

public enum PlayerAgent implements State<AgentStateComponent> {

    DEFAULT() {
        @Override
        public void enter(AgentStateComponent agentStateComponent) {
            System.out.println("Player NORMAL");
        }

        @Override
        public void exit(AgentStateComponent agentStateComponent) {
            System.out.println("Player NORMAL");
        }

        @Override
        public void update(AgentStateComponent agentStateComponent) {

        }

        @Override
        public float getFrameRatio() {
            return 0;
        }

        @Override
        public String getName() {
            return "default";
        }
    },

    DASH() {
        Float frames;
        Float currentFrame;

        @Override
        public void enter(AgentStateComponent agentStateComponent) {
            System.out.println("Player DASH");
            frames = 14f;
            currentFrame = frames;
        }

        @Override
        public void exit(AgentStateComponent agentStateComponent) {
            System.out.println("Player DASH");
        }

        @Override
        public void update(AgentStateComponent agentStateComponent) {
            currentFrame--;
            if (currentFrame <= 0) {
                if (agentStateComponent.revertToPreviousState()) {
                    System.out.println("Player reverted to " + agentStateComponent.getCurrentState().toString());
                }
            }
        }

        @Override
        public float getFrameRatio() {
            return currentFrame / frames;
        }

        @Override
        public String getName() {
            return "dash";
        }
    },

    ATTACK() {
        Float frames;
        Float currentFrame;

        @Override
        public void enter(AgentStateComponent agentStateComponent) {
            System.out.println("Player ATTACK");
            frames = 16f;
            currentFrame = frames;
        }

        @Override
        public void exit(AgentStateComponent agentStateComponent) {
            System.out.println("Player ATTACK");
        }

        @Override
        public void update(AgentStateComponent agentStateComponent) {
            currentFrame--;
            if (currentFrame <= 0) {
                if (agentStateComponent.revertToPreviousState()) {
                    System.out.println("Player reverted to " + agentStateComponent.getCurrentState().toString());
                }
            }
        }

        @Override
        public float getFrameRatio() {
            return currentFrame / frames;
        }

        @Override
        public String getName() {
            return "attack";
        }
    }
}
