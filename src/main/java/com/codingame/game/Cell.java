package com.codingame.game;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.tooltip.TooltipModule;

public class Cell {
	private GraphicEntityModule graphicEntityModule;
	private TooltipModule tooltips;

	private int x;
	private int y;

	private Group displayGroup;

	public Cell(int x, int y, GraphicEntityModule graphicEntityModule, TooltipModule tooltips) {
		this.graphicEntityModule = graphicEntityModule;
		this.tooltips = tooltips;
		this.x = x;
		this.y = y;
		int srcX = x * 150 + 64;
		int srcY = y * 150 + 248;
		Sprite computerSprite = graphicEntityModule.createSprite()
			.setImage("computer.png")
			.setX(srcX)
			.setY(srcY)
			.setAnchor(0);
		displayGroup = graphicEntityModule.createGroup(computerSprite);
		this.updateToolTip();
	}

	public void updateDisplay() {

	}

	private void updateToolTip() {
		tooltips.setTooltipText(displayGroup, "Computer " + x + ";" + y);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
		if (!(other instanceof Cell))return false;
		Cell otherCell = (Cell)other;
		return otherCell.x == x && otherCell.y == y;
	}
}
