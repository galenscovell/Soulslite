package galenscovell.soulslite.processing.fsm;


import galenscovell.soulslite.actors.components.StateComponent;

public enum EnemyState implements State<StateComponent> {

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
    }
}
