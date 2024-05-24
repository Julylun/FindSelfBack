package com.findselfback.GameState;

import com.findselfback.View.GamePlayPanel;

public class State {
    protected GamePlayPanel gamePlayPanel;
    public State(GamePlayPanel gamePlayPanel){
        this.gamePlayPanel = gamePlayPanel;
    }

    public GamePlayPanel getGamePlayPanel() {
        return gamePlayPanel;
    }
}
