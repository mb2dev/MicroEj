package moc.esgi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.microui.display.Colors;
import ej.microui.display.Display;
import ej.microui.display.Displayable;
import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Image;
import ej.microui.display.transform.ImageRotation;
import ej.microui.event.Event;
import ej.microui.event.generator.Pointer;
import ej.microui.util.EventHandler;

public class MyDisplayable extends Displayable implements EventHandler {

	private final static Random rdm = new Random();
	
	int dimH;
	int dimV;
	
	boolean click = false;
	
	int pY = 0;
	int pX = 0;
	int periodCounter = 0;
	int modulo = 33;
	
	// animation
	boolean bounce;
	int dX;
	int dY;
	
	private Image fruitNinjaBackground;
	private ArrayList<Fruit> fruits;
	
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
		
		fruits = new ArrayList<Fruit>();
		
		bounce = false;
		dX = 3;
		dY = 3;
		Timer t = new Timer();
		t.schedule(new TimerTask(){
			@Override
			public void run(){
				/*if(bounce){
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
				}*/
				//System.out.println("update UI");
				
				// For safe remove : https://stackoverflow.com/questions/1196586/calling-remove-in-foreach-loop-in-java
				
				
				//fruits.
				repaint();
				
			}
		}, 0, 100);
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
	public void paint(GraphicsContext gc) {
		
		Iterator<Fruit> i = fruits.iterator();
		
		ArrayList<Fruit> parts = new ArrayList<Fruit>();
		while (i.hasNext()) {
			Fruit f = i.next();
			f.update();
			if(f.toDelete()){
				System.out.println("deleting "+f.toString());
				i.remove();
			}
			if(click){
				if(f.intersect(pX, pY) && !f.isPart){
					i.remove();
					parts = f.breakFruit();
					click = false;
				}
				
			}
		}
		fruits.addAll(parts);
		
		
		if((periodCounter % modulo) == 0){
			// on change la valeur du modulo pour que les fruits
			// apparaissent de façon irrégulière
			modulo = rdm.nextInt(33) + 10;
			try{
				fruits.add(new Fruit(dimH, dimV));
			}catch(IOException e){
				throw new AssertionError(e);
			}
		}
		
		//System.out.println("fruits.size = " + fruits.size());

		gc.drawImage(fruitNinjaBackground, 0, 0, 0);
		
		Iterator<Fruit> j = fruits.iterator();
		while (j.hasNext()) {
			Fruit f = j.next();
			ImageRotation rotation = new ImageRotation();
			rotation.setRotationCenter((int)f.pos[0], (int)f.pos[1]);
			rotation.setAngle((int)f.pos[2] % 360);
			rotation.drawNearestNeighbor(gc, f.img, (int)f.pos[0], (int)f.pos[1], GraphicsContext.HCENTER | GraphicsContext.VCENTER);
			//gc.drawImage(f.img, (int)f.pos[0], (int)f.pos[1], GraphicsContext.HCENTER | GraphicsContext.VCENTER);
		}
		periodCounter++;
	}

	@Override
	public EventHandler getController() {
		return this;
	}

}
