package galenscovell.soulslite.processing.fsm;


import galenscovell.soulslite.actors.components.AgentStateComponent;

public enum RangedAgent implements State<AgentStateComponent> {

    DEFAULT() {
        @Override
        public void enter(AgentStateComponent agentStateComponent) {
            System.out.println("Enemy NORMAL");
        }

        @Override
        public void exit(AgentStateComponent agentStateComponent) {
            System.out.println("Enemy NORMAL");
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

    PURSUE() {
        @Override
        public void enter(AgentStateComponent agentStateComponent) {
            System.out.println("Enemy PURSUE");
        }

        @Override
        public void exit(AgentStateComponent agentStateComponent) {
            System.out.println("Enemy PURSUE");
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
            return "pursue";
        }
    },

    EVADE() {
        @Override
        public void enter(AgentStateComponent agentStateComponent) {
            System.out.println("Enemy EVADE");
        }

        @Override
        public void exit(AgentStateComponent agentStateComponent) {
            System.out.println("Enemy EVADE");
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
            return "evade";
        }
    },

    ATTACK() {
        Float frames;
        Float currentFrame;

        @Override
        public void enter(AgentStateComponent agentStateComponent) {
            System.out.println("Enemy ATTACK");
            frames = 16f;
            currentFrame = frames;
        }

        @Override
        public void exit(AgentStateComponent agentStateComponent) {
            System.out.println("Enemy ATTACK");
        }

        @Override
        public void update(AgentStateComponent agentStateComponent) {
            currentFrame--;
            if (currentFrame <= 0) {
                if (agentStateComponent.revertToPreviousState()) {
                    System.out.println("Enemy reverted to " + agentStateComponent.getCurrentState().toString());
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
