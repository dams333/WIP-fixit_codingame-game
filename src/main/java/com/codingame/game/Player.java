package com.codingame.game;
import java.util.ArrayList;
import java.util.List;

import com.codingame.game.commands.Command;
import com.codingame.game.commands.HireCommand;
import com.codingame.game.commands.InvalidCommandException;
import com.codingame.game.commands.MoveCommand;
import com.codingame.game.commands.WaitCommand;
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
	
    @Override
    public int getExpectedOutputLines() {
        return 1;
    }

	public void initHud(int x) {
		setScore(100);
		Text name = graphicEntityModule.createText(getNicknameToken())
                    .setX(x + 370)
                    .setY(25)
                    .setZIndex(20)
                    .setFontSize(30)
                    .setFillColor(0xffffff)
                    .setAnchor(0.5);

		creditsText = graphicEntityModule.createText("" + getScore())
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
		creditsText.setText("" + getScore());
	}

	public List<Command> parse(String input, Grid grid) throws InvalidCommandException {
		List<Command> commands = new ArrayList<>();

		String[] cmds = input.split(";");
		if (cmds.length == 0) {
			throw new InvalidCommandException("No command");
		}
		for (String cmd : cmds) {
			String[] args = cmd.split(" ");
			if (args.length == 0) {
				throw new InvalidCommandException("Empty command");
			}
			if (args[0].equals("WAIT")) {
				if (args.length != 1) {
					throw new InvalidCommandException("Bad usage: WAIT");
				}
				commands.add(new WaitCommand());
			} else if (args[0].equals("MOVE")) {
				if (args.length != 4) {
					throw new InvalidCommandException("Bad usage: MOVE <id> <x> <y>");
				}
				try {
					int id = Integer.parseInt(args[1]);
					int x = Integer.parseInt(args[2]);
					int y = Integer.parseInt(args[3]);
					if (!grid.isPlayerFixer(id, getIndex())) {
						throw new InvalidCommandException("You can't move the fixer " + id);
					}
					if (!grid.isValidPosition(x, y)) {
						throw new InvalidCommandException("Invalid position (" + x + ", " + y + ")");
					}
					commands.add(new MoveCommand(id, x, y));
				} catch (NumberFormatException e) {
					throw new InvalidCommandException("Bad usage: MOVE <id> <x> <y>");
				}
			} else if (args[0].equals("HIRE")) {
				if (args.length != 3) {
					throw new InvalidCommandException("Bad usage: HIRE <x> <y>");
				}
				try {
					int x = Integer.parseInt(args[1]);
					int y = Integer.parseInt(args[2]);
					if (!grid.isValidPosition(x, y)) {
						throw new InvalidCommandException("Invalid position (" + x + ", " + y + ")");
					}
					commands.add(new HireCommand(x, y));
				} catch (NumberFormatException e) {
					throw new InvalidCommandException("Bad usage: HIRE <x> <y>");
				}
			} else {
				throw new InvalidCommandException("Unknown command '" + args[0] + "'");
			}
		}

		return commands;
	}
}
