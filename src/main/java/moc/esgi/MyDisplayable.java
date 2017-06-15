package moc.esgi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
	
	int imgX = 0;
	int imgY = 0;
	int pY = 0;
	int pX = 0;
	int periodCounter = 0;
	int modulo = 33;
	
	// animation
	boolean bounce;
	int dX;
	int dY;
	
	int score = 0;
	
	private final Font font = Font.getFont(Font.LATIN, 26, Font.STYLE_PLAIN);
	private final Image fruitNinjaBackground;
	private final Image scoreMelon;
	private ArrayList<Fruit> fruits;
	private ArrayList<FNPoints> labels;
	
	public MyDisplayable() {
		super(Display.getDefaultDisplay());
		dimV = Display.getDefaultDisplay().getHeight();
		dimH = Display.getDefaultDisplay().getWidth();
		try{
			scoreMelon = Image.createImage("/images/Fruits/Menu/score_melon_xs.png");
			fruitNinjaBackground = Image.createImage("/images/fruit_background.png");
		}
		catch(IOException e){
			throw new AssertionError(e);
		}
		
		labels = new ArrayList<FNPoints>();
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
				return true;
			}
			
		}
		return false;
	}

	// Appelée au démarrage et lors d'un repaint
	@Override
	public void paint(GraphicsContext gc) {
		
		gc.drawImage(fruitNinjaBackground, 0, 0, 0);
		gc.drawImage(scoreMelon, 0, 0, 0);
		gc.setColor(0xfae74c);
		gc.setFont(font);
		
		Iterator<Fruit> i = fruits.iterator();
		
		ArrayList<Fruit> parts = new ArrayList<Fruit>();
		while (i.hasNext()) {
			Fruit f = i.next();
			if(!click)
			{
				f.update();
				if(f.toDelete()){
					//System.out.println("deleting "+f.toString());
					i.remove();
				}
			}
			else
			{
				if(f.intersect(pX, pY) && !f.isPart){
					click = false;
					i.remove();
					score += f.value;
				
					/**
					 * Conflit avec un repaint parallèle
					 * cf. iterator in labels plus bas
					 * 
					 * ConcurrentModificationException
					 */
					labels.add(
							new FNPoints(
									new String("+"+f.value), 
									new Position(f.getX(), f.getY())
							)
					);
					parts = f.breakFruit();
					fruits.addAll(parts);
				}
			}
		}
		
		
		
		if((periodCounter % modulo) == 0 && fruits.size() < 8){
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
		String str = new String(""+score);
		gc.drawString(str, scoreMelon.getWidth()+10, scoreMelon.getHeight()/4, 0);

		
		Iterator<Fruit> j = fruits.iterator();
		while (j.hasNext()) {
			Fruit f = j.next();
			ImageRotation rotation = new ImageRotation();
			rotation.setRotationCenter((int)f.pos[0], (int)f.pos[1]);
			rotation.setAngle((int)f.pos[2] % 360);
			rotation.drawNearestNeighbor(gc, f.img, (int)f.pos[0], (int)f.pos[1], GraphicsContext.HCENTER | GraphicsContext.VCENTER);
			//gc.drawImage(f.img, (int)f.pos[0], (int)f.pos[1], GraphicsContext.HCENTER | GraphicsContext.VCENTER);
		}
		
		Iterator<FNPoints> it = labels.iterator();
		while(it.hasNext()){
			FNPoints p = it.next();
			gc.drawString(p.display(), p.pos.X++, p.pos.Y--, 0);
			if(p.toDelete()){
				it.remove();
			}
		}
		
		periodCounter++;
	}

	@Override
	public EventHandler getController() {
		return this;
	}

}
