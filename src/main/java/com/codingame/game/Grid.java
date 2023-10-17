package com.codingame.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;

public class Grid {
	@Inject private GraphicEntityModule graphicEntityModule;
	@Inject private TooltipModule tooltips;

	private final int gridWidth = 12;
	private final int gridHeight = 5;

	private List<List<Cell>> grid;
	private List<Fixer> fixers;

	private Random random;
	private MultiplayerGameManager<Player> gameManager;

	public void initGrid(Random random, MultiplayerGameManager<Player> gameManager) {
		this.gameManager = gameManager;
		this.random = random;
		fixers = new ArrayList<Fixer>();
		for (Player p : gameManager.getActivePlayers()) {
			fixers.add(new Fixer(0, 0, p.getIndex(), graphicEntityModule, tooltips));
		}

		grid = new ArrayList<List<Cell>>(gridWidth);
		for(int x = 0; x < gridWidth; x++) {
			grid.add(new ArrayList<Cell>(gridHeight));
			for(int y = 0; y < gridHeight; y++) {
				grid.get(x).add(new Cell(x, y, graphicEntityModule, tooltips, random));
			}
		}
	}

	public void update() {
		int cell = random.nextInt(gridWidth * gridHeight);
		grid.get(cell % gridWidth).get(cell / gridWidth).spawnBug();

		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridHeight; y++) {
				grid.get(x).get(y).update();
			}
		}
	}

	public List<String> export() {
		List<String> gridExport = new ArrayList<String>(gridWidth * gridHeight);
		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridHeight; y++) {
				gridExport.add(grid.get(x).get(y).export());
			}
		}
		return gridExport;
	}

	public boolean isPlayerFixer(int fixerId, int playerIndex) {
		for (Fixer fixer : fixers) {
			if (fixer.getId() == fixerId && fixer.getPlayerIndex() == playerIndex) {
				return true;
			}
		}
		return false;
	}

	public boolean isValidPosition(int x, int y) {
		return x >= 0 && x < gridWidth && y >= 0 && y < gridHeight;
	}

	public void moveFixer(int id, int x, int y) {
		for (Fixer fixer : fixers) {
			if (fixer.getId() == id) {
				fixer.move(x, y);
				return;
			}
		}
	}

	public int getMapWidth() {
		return gridWidth;
	}
	public int getMapHeight() {
		return gridHeight;
	}

	public List<String> exportFixers(int playerIndex) {
		List<String> fixersExport = new ArrayList<String>(fixers.size());
		for (Fixer fixer : fixers) {
			fixersExport.add(fixer.export(playerIndex));
		}
		return fixersExport;
	}
}