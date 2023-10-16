package com.codingame.game;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;
import com.google.inject.Inject;

public class Player extends AbstractMultiplayerPlayer {
	@Inject private GraphicEntityModule graphicEntityModule;

	public Group hud;
	Text creditsText;

	private int credits = 100;
	
    @Override
    public int getExpectedOutputLines() {
        return 1;
    }

	public int getCredits() {
		return credits;
	}

	public void initHud(int x) {
		Text name = graphicEntityModule.createText(getNicknameToken())
                    .setX(x + 370)
                    .setY(25)
                    .setZIndex(20)
                    .setFontSize(30)
                    .setFillColor(0xffffff)
                    .setAnchor(0.5);

		creditsText = graphicEntityModule.createText("" + getCredits())
                    .setX(x + 240)
                    .setY(90)
                    .setZIndex(20)
                    .setFontSize(30)
                    .setFillColor(0xffffff)
                    .setAnchor(0);

        Sprite avatar = graphicEntityModule.createSprite()
                    .setX(x)
                    .setY(0)
                    .setZIndex(20)
                    .setImage(getAvatarToken())
                    .setAnchor(0)
                    .setBaseHeight(130)
                    .setBaseWidth(130);

        hud = graphicEntityModule.createGroup(name, creditsText, avatar);
	}

	public void updateHud() {
		creditsText.setText("" + getCredits());
	}
}
