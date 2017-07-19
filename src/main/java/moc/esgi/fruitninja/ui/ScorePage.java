package moc.esgi.fruitninja.ui;

import ej.widget.container.Grid;
import ej.widget.navigation.page.Page;

public class ScorePage extends Page {
	
	Grid grid;
	private DisplayableScore displayablescore;
	
	
	public ScorePage(){
		super();
		grid = new Grid();
		grid.add(new DisplayableScore());
		this.setWidget(grid);
	}

}
