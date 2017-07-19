package moc.esgi.fruitninja.ui;

import ej.widget.container.Grid;
import ej.widget.navigation.page.Page;

public class MenuPage extends Page {
	
	Grid grid;	
	
	public MenuPage(){
		super();
		grid = new Grid();
		grid.add(new DisplayableMenu());
		this.setWidget(grid);
		
	}

}
