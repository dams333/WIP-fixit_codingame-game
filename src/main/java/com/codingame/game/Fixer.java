package com.codingame.game;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.tooltip.TooltipModule;

public class Fixer {
	private static int nextId = 0;
	private int id;
	private int x;
	private int y;
	private int ownerIndex;
	private GraphicEntityModule graphicEntityModule;
	private TooltipModule tooltips;
	private Sprite sprite;

	public Fixer(int x, int y, int ownerIndex, GraphicEntityModule graphicEntityModule, TooltipModule tooltips) {
		this.graphicEntityModule = graphicEntityModule;
		this.tooltips = tooltips;
		this.x = x;
		this.y = y;
		this.ownerIndex = ownerIndex;
		this.id = nextId++;

		sprite = graphicEntityModule.createSprite()
			.setImage(ownerIndex == 0 ? "red_fixer.png" : "blue_fixer.png")
			.setX(0)
			.setY(0)
			.setAnchor(0)
			.setZIndex(3);
		tooltips.setTooltipText(sprite, "Fixer " + id);
		this.update();
	}

	public void update() {
		graphicEntityModule.commitEntityState(0, sprite);
		sprite.setX(x * 150 + 64 + (ownerIndex == 0 ? 10 : 150 - 64 - 10));
		sprite.setY(y * 150 + 248 + 10);
		graphicEntityModule.commitEntityState(0.3, sprite);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Fixer) {
			Fixer other = (Fixer)obj;
			return this.id == other.id;
		}
		return false;
	}

	public boolean isOnCase(int x, int y){
		return this.x == x && this.y == y;
	}

	public int getId() {
		return id;
	}
	public int getPlayerIndex() {
		return ownerIndex;
	}

	public void move(int x, int y) {
		this.x = x;
		this.y = y;
		this.update();
	}

	public String export(int playerIndex) {
		return String.format("%d %d %d %d", id, x, y, playerIndex == ownerIndex ? 1 : -1);
	}
}