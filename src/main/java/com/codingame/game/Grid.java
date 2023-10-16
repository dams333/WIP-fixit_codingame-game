package com.codingame.game;

import java.util.ArrayList;
import java.util.List;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;

public class Grid {
	@Inject private GraphicEntityModule graphicEntityModule;
	@Inject private TooltipModule tooltips;

	private List<List<Cell>> grid;

	public void initGrid() {
		grid = new ArrayList<List<Cell>>(12);
		for(int x = 0; x < 12; x++) {
			grid.add(new ArrayList<Cell>(5));
			for(int y = 0; y < 5; y++) {
				grid.get(x).add(new Cell(x, y, graphicEntityModule, tooltips));
			}
		}
	}

	public void updateGrid() {
		for (int x = 0; x < 12; x++) {
			for (int y = 0; y < 5; y++) {
				grid.get(x).get(y).updateDisplay();
			}
		}
	}
}
