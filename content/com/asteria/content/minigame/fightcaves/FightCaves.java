package com.asteria.content.minigame.fightcaves;

import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.minigame.SequencedMinigame;

public class FightCaves extends SequencedMinigame {

    // TODO: Just a placeholder, I'll finish this soon.

    public FightCaves() {
        super("Fight Caves");
    }

    @Override
    public void onSequence() {
        // TODO Auto-generated method stub

    }

    @Override
    public int delay() {
        // TODO Auto-generated method stub
        return 50000;
    }

    @Override
    public void onLogin(Player player) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLogout(Player player) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean contains(Player player) {
        // TODO Auto-generated method stub
        return false;
    }

}
