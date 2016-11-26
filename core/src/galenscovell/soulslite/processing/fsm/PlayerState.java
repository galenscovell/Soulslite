package galenscovell.soulslite.processing.fsm;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;


public enum PlayerState implements State<PlayerAgent> {

    NORMAL() {
        @Override
        public void update(PlayerAgent entity) {

        }
    },

    DASHING {
        @Override
        public void update(PlayerAgent entity) {

        }
    };


    @Override
    public void enter(PlayerAgent entity) {

    }

    @Override
    public void exit(PlayerAgent entity) {

    }

    @Override
    public boolean onMessage(PlayerAgent entity, Telegram telegram) {
        return false;
    }
}
