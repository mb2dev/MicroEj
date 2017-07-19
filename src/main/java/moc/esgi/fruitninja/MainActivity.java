package moc.esgi.fruitninja;

import ej.microui.MicroUI;
import ej.mwt.Desktop;
import ej.mwt.Panel;
import ej.wadapps.app.Activity;
import ej.widget.navigation.navigator.SimpleNavigator;
import ej.widget.navigation.transition.HorizontalTransitionManager;
import moc.esgi.fruitninja.ui.MenuPage;

public class MainActivity implements Activity {
	
	public static SimpleNavigator nav = new SimpleNavigator();

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
		nav.setTransitionManager(new HorizontalTransitionManager());
		nav.show(MenuPage.class.getName(), false);
		
		Desktop desk = new Desktop();
		Panel mainPanel = new Panel();
		
		mainPanel.setWidget(nav);
		mainPanel.show(desk, true);
		desk.show();
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
