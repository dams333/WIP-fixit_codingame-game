package com.codingame.game;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;
import com.codingame.gameengine.module.entities.Group;

public class Player extends AbstractMultiplayerPlayer {
	public Group hud;

	private int credits = 0;
	
    @Override
    public int getExpectedOutputLines() {
        return 1;
    }

	public int getCredits() {
		return credits;
	}
}
