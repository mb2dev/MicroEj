package moc.esgi;

import ej.microui.MicroUI;
import ej.microui.display.Colors;
import ej.microui.display.Display;
import ej.microui.display.GraphicsContext;
import ej.mwt.Desktop;
import ej.mwt.Panel;
import ej.style.Stylesheet;
import ej.style.border.Border;
import ej.style.border.SimpleRoundedBorder;
import ej.style.outline.SimpleOutline;
import ej.style.selector.TypeSelector;
import ej.style.util.EditableStyle;
import ej.style.util.StyleHelper;
import ej.wadapps.app.Activity;
import ej.widget.basic.Label;
import ej.widget.navigation.navigator.SimpleNavigator;
import ej.widget.navigation.transition.HorizontalTransitionManager;

public class MainActivity implements Activity {
	
	public static SimpleNavigator nav = new SimpleNavigator();
	Menu myP;

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRestart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		MicroUI.start();
		myP = new Menu(Display.getDefaultDisplay());
		myP.show();
		/*nav.setTransitionManager(new HorizontalTransitionManager());
		nav.show(MainPage.class.getName(), true);
		
		Desktop desk = new Desktop();
		Panel mainPanel = new Panel();
		
		
		mainPanel.setWidget(nav);
		mainPanel.show(desk,true); // fit to desktop size and will be declared as not be packed
		// set specific style
		SimpleOutline line = new SimpleOutline(12);

		
		Stylesheet sts = StyleHelper.getStylesheet();
		EditableStyle myStyle = new EditableStyle();
	
		myStyle.setAlignment(GraphicsContext.HCENTER | GraphicsContext.VCENTER);
		myStyle.setForegroundColor(Colors.WHITE);
		myStyle.setBackgroundColor(Colors.BLACK);
		myStyle.setMargin(line);
		myStyle.setPadding(line);

		Border border = new SimpleRoundedBorder(2, 2);
		myStyle.setBorder(border);
		myStyle.setBorderColor(Colors.WHITE);
		sts.addRule(new TypeSelector(Label.class), myStyle);
	    desk.show();*/
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}
	

}
