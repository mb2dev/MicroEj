package moc.esgi.fruitninja.ui;

import java.io.IOException;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.exit.ExitHandler;
import ej.microui.display.Colors;
import ej.microui.display.Display;
import ej.microui.display.Displayable;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Image;
import ej.microui.util.EventHandler;
import ej.style.background.NoBackground;
import ej.style.background.SimpleImageBackground;
import ej.style.outline.SimpleOutline;
import ej.style.selector.TypeSelector;
import ej.style.selector.combinator.ChildCombinator;
import ej.style.util.EditableStyle;
import ej.style.util.StyleHelper;
import ej.widget.basic.Label;
import ej.widget.composed.Button;
import ej.widget.container.Dock;
import ej.widget.container.Grid;
import ej.widget.container.List;
import ej.widget.container.Scroll;
import ej.widget.container.Split;
import ej.widget.listener.OnClickListener;
import ej.widget.navigation.page.Page;


public class MainPage extends Page  {
	
	Split mySplit;
	List myList;
	Scroll scroll;
	Grid grid;
	private MyDisplayable displayable;

	public MainPage() {
		// TODO Auto-generated constructor stub
		super();
		mySplit = new Split();
		mySplit.setHorizontal(false);
		myList=new List();
		myList.setHorizontal(false);
		scroll = new Scroll(false,true);
		grid = new Grid();
		
		try {
			EditableStyle gridStyle = new EditableStyle();
			TypeSelector gridSe1 = new TypeSelector(Grid.class);
			SimpleImageBackground myBackground = new SimpleImageBackground(Image.createImage("/images/fruit_background.png"),
					GraphicsContext.HCENTER | GraphicsContext.VCENTER ,true);
			
			//gridStyle.setBackground(myBackground);
			StyleHelper.getStylesheet().addRule(gridSe1, gridStyle);
			//SimpleOutline myPadding = new SimpleOutline(12);// image integrity
			//lb1Style.setPadding(myPadding);
			//lb1Style.setForegroundColor(Colors.NAVY);
			

			EditableStyle tt = new EditableStyle();
			tt.setAlignment(GraphicsContext.HCENTER | GraphicsContext.VCENTER);
			tt.setBackgroundColor(Colors.WHITE);
			tt.setForegroundColor(Colors.WHITE);
			TypeSelector btnSe2 = new TypeSelector(Split.class);
			StyleHelper.getStylesheet().addRule( btnSe2, tt);
			
			EditableStyle transp = new EditableStyle(); // transparent Label if child of a button
			transp.setBackground(NoBackground.NO_BACKGROUND);
			TypeSelector lblSel = new TypeSelector(Label.class);
			//ChildCombinator parentBtnSel = new ChildCombinator(btnSe1, lblSel); // button>label
			//StyleHelper.getStylesheet().addRule(parentBtnSel, transp);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Button btn1 = new Button("btn1");
		Button btn3 = new Button("btn3");
		Button btn2 = new Button("Exit");
		
	
		//mySplit.setFirst(btn1);
		//mySplit.setLast(btn2);
		
		
		myList.add(btn1);
		myList.add(btn2);
		myList.add(btn3);
		
	
		
		btn2.addOnClickListener(new OnClickListener() {
			@Override
			  public void onClick()
			  {
			   //use the ExitHandler to stop application
			System.out.println("Exit"); 
			ExitHandler exitHandler = ServiceLoaderFactory.getServiceLoader().getService(ExitHandler.class); 
			if (exitHandler != null)
			{
			       exitHandler.exit();
			    }
			} });
		scroll.setWidget(myList);
		grid.add(new MyDisplayable());
		
		this.setWidget(grid);
	}


	
}
