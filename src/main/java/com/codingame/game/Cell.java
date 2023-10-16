package com.codingame.game;

import java.util.Random;

import com.codingame.gameengine.module.entities.Circle;
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
	private Circle bugCircle;
	private int bugLevel = 0;
	private Random random;

	private int turnToUpgradeBug = 0;

	public Cell(int x, int y, GraphicEntityModule graphicEntityModule, TooltipModule tooltips, Random random) {
		this.random = random;
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
			.setAnchor(0)
			.setZIndex(1);
		bugCircle = graphicEntityModule.createCircle()
			.setX(srcX + 150/2)
			.setY(srcY + 150/2)
			.setRadius(150/2 - 20)
			.setFillColor(0x000000)
			.setAlpha(0)
			.setZIndex(2);
		displayGroup = graphicEntityModule.createGroup(computerSprite, bugCircle);
		this.updateDisplay();
	}

	public void spawnBug() {
		if (bugLevel == 0) {
			bugLevel = 1;
			turnToUpgradeBug = random.nextInt(3) + 2;
		}
	}

	public void update() {
		if (turnToUpgradeBug > 0) {
			turnToUpgradeBug--;
			if (turnToUpgradeBug == 0) {
				if (bugLevel < 3)
					bugLevel++;
				if (bugLevel < 3)
					turnToUpgradeBug = random.nextInt(3) + 2;
			}
		}

		this.updateDisplay();
	}

	private void updateDisplay() {
		if (bugLevel > 0) {
			bugCircle.setAlpha(0.5);
			switch (bugLevel) {
				case 1:
					bugCircle.setFillColor(0x00FF00);
					break;
				case 2:
					bugCircle.setFillColor(0xFFFF00);
					break;
				case 3:
					bugCircle.setFillColor(0xFF0000);
					break;
			}
		} else {
			bugCircle.setAlpha(0);
		}
		this.updateToolTip();
	}

	private void updateToolTip() {
		String tooltipText = "Computer " + x + ";" + y;
		if (bugLevel > 0)
			tooltipText += "\nBug level: " + bugLevel;
		tooltips.setTooltipText(displayGroup, tooltipText);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
		if (!(other instanceof Cell))return false;
		Cell otherCell = (Cell)other;
		return otherCell.x == x && otherCell.y == y;
	}

	public String export() {
		return String.format("%d %d %d", x, y, bugLevel);
	}
}
