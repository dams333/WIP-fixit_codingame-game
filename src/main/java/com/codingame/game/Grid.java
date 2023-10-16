package com.codingame.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;

public class Grid {
	@Inject private GraphicEntityModule graphicEntityModule;
	@Inject private TooltipModule tooltips;

	private final int gridWidth = 12;
	private final int gridHeight = 5;

	private List<List<Cell>> grid;

	private Random random;

	public void initGrid(Random random) {
		this.random = random;
		grid = new ArrayList<List<Cell>>(gridWidth);
		for(int x = 0; x < gridWidth; x++) {
			grid.add(new ArrayList<Cell>(gridHeight));
			for(int y = 0; y < gridHeight; y++) {
				grid.get(x).add(new Cell(x, y, graphicEntityModule, tooltips));
			}
		}
	}

	public void updateGrid() {
		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridHeight; y++) {
				grid.get(x).get(y).updateDisplay();
			}
		}
	}
}
