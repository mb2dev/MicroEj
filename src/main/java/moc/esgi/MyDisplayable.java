package moc.esgi;

import java.io.IOException;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.microui.display.Colors;
import ej.microui.display.Display;
import ej.microui.display.Displayable;
import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Image;
import ej.microui.event.Event;
import ej.microui.event.generator.Pointer;
import ej.microui.util.EventHandler;

public class MyDisplayable extends Displayable implements EventHandler {

	int dimH;
	int dimV;
	
	boolean click = false;
	
	int pY = 0;
	int pX = 0;
	
	// animation
	boolean bounce;
	int dX;
	int dY;
	
	private Image fruitNinjaBackground;
	int imgX = 0;
	int imgY = 0;
	
	private final Font font = Font.getFont(Font.LATIN, 26, Font.STYLE_PLAIN);
	
	public MyDisplayable() {
		super(Display.getDefaultDisplay());
		dimV = Display.getDefaultDisplay().getHeight();
		dimH = Display.getDefaultDisplay().getWidth();
		try{
			fruitNinjaBackground = Image.createImage("/images/fruit_background.png");
		}
		catch(IOException e){
			throw new AssertionError(e);
		}
		
		bounce = false;
		dX = 3;
		dY = 3;
		Timer t = new Timer();
		t.schedule(new TimerTask(){
			@Override
			public void run(){
				if(bounce){
					if(imgX + fruitNinjaBackground.getWidth()/2 > dimH)
						dX = -Math.abs(dX);
					
					if(imgX - fruitNinjaBackground.getWidth()/2 < 0)
						dX = Math.abs(dX);
					
					if(imgY + fruitNinjaBackground.getHeight()/2 > dimV)
						dY = -Math.abs(dY);
					if(imgY - fruitNinjaBackground.getHeight()/2 < 0)
						dY = Math.abs(dY);
					
					imgX += dX;
					imgY += dY;
					repaint();
				}
			}
		}, 0, 30);
	}

	@Override
	public boolean handleEvent(int event) {
		if(Event.getType(event) == Event.POINTER){
			if(Pointer.isPressed(event))
			{
				click = true;
				bounce = false;
				Pointer ptr = (Pointer)Event.getGenerator(event);
				imgX = pX = ptr.getX();
				imgY = pY = ptr.getY();
				repaint();
			}else
				bounce = true;
			
		}else{ // button
			bounce = false;
			click = false;
			repaint();
		}
		return false;
	}

	// Appelée au démarrage et lors d'un repaint
	@Override
	public void paint(GraphicsContext g) {
		
		System.out.println("bounce :" + bounce +", click: "+click);
		System.out.println("pX :"+pX+", pY:"+pY);
		System.out.println("imgX :"+imgX+", imgY"+imgY);
		if(click){
			
			g.setColor(Colors.WHITE);
			g.fillRect(0, 0, dimH, dimV);
			
			//g.drawImage(microejImage, pX, pY, GraphicsContext.HCENTER | GraphicsContext.VCENTER);
			click = false;
			bounce = true;
			
		}else if(bounce){
			g.setColor(Colors.WHITE);
			g.fillRect(0, 0, dimH, dimV);
			//g.drawImage(microejImage, imgX, imgY, GraphicsContext.HCENTER | GraphicsContext.VCENTER);
		}else{
			// background
			//g.setColor(Colors.WHITE);
			g.drawImage(fruitNinjaBackground, 0, 0, 0);
			//g.fillRect(0, 0, dimH, dimV);
		}
	}

	@Override
	public EventHandler getController() {
		return this;
	}

}
