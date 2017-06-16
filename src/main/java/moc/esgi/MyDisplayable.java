package moc.esgi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
	private final static int GAME_LENGTH = 10;
	private final static int DELAY = 3;
	
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
	int second_counter = GAME_LENGTH;
	boolean stop = false;
	
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
			scoreMelon = Image.createImage("/images/Fruits/Menu/score_melon_xxs.png");
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
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){
			@Override
			public void run(){
				second_counter--;
			}
		}, 0, 1000);
		
		resetTimer();
	}
	
	void resetTimer(){
		Timer t = new Timer();
		t.schedule(new TimerTask(){
			@Override
			public void run(){
				if(isStopped()){
					t.cancel();
				}
				//fruits
				repaint();
				
			}
		}, 0, 50);
	}

	@Override
	public boolean handleEvent(int event) {
		if(Event.getType(event) == Event.POINTER){
			if(Pointer.isPressed(event))
			{
				click = true;
				Pointer ptr = (Pointer)Event.getGenerator(event);
				imgX = pX = ptr.getX();
				imgY = pY = ptr.getY();
				repaint();
				return true;
			}
			if(Pointer.isDoubleClicked(event) && isStopped()){
				second_counter = GAME_LENGTH;
				score = 0;
				resetTimer();
				return true;
			}
			
		}
		return false;
	}

	public boolean isStopped(){
		return second_counter <= -DELAY;
	}
	
	// Appelée au démarrage et lors d'un repaint
	@Override
	public void paint(GraphicsContext gc) {
		
		gc.drawImage(fruitNinjaBackground, 0, 0, 0);
		gc.drawImage(scoreMelon, 0, 0, 0);
		gc.setColor(0xfae74c);
		gc.setFont(font);
		
		
		if(!isStopped()){
			updateFruits(gc);
			generateFruits();
			
			drawFruits(gc);
			drawPoints(gc);
		}else{
			gc.drawString("SCORE : "+score, dimH/2, dimV/2, GraphicsContext.HCENTER | GraphicsContext.VCENTER);
		}
		updateInfo(gc);
	}
	
	void drawPoints(GraphicsContext gc){
		Iterator<FNPoints> it = labels.iterator();
		while(it.hasNext()){
			FNPoints p = it.next();
			gc.drawString(p.display(), p.pos.X++, p.pos.Y--, 0);
			if(p.toDelete()){
				it.remove();
			}
		}
	}
	
	void drawFruits(GraphicsContext gc){
		Iterator<Fruit> j = fruits.iterator();
		while (j.hasNext()) {
			Fruit f = j.next();
			ImageRotation rotation = new ImageRotation();
			rotation.setRotationCenter((int)f.pos[0], (int)f.pos[1]);
			rotation.setAngle((int)f.pos[2] % 360);
			rotation.drawNearestNeighbor(gc, f.img, (int)f.pos[0], (int)f.pos[1], GraphicsContext.HCENTER | GraphicsContext.VCENTER);
			//gc.drawImage(f.img, (int)f.pos[0], (int)f.pos[1], GraphicsContext.HCENTER | GraphicsContext.VCENTER);
		}
	}
	
	void updateInfo(GraphicsContext gc){
		//System.out.println("fruits.size = " + fruits.size());
		String score_str = new String(""+score);
		gc.drawString(score_str, scoreMelon.getWidth()+10, scoreMelon.getHeight()/4, 0);
		
		int min = Math.max(0, second_counter / 60);
		int sec = Math.max(0, second_counter % 60);
		String timer_str = new String(min + " : " + (sec < 10 ? "0"+sec : sec));
		gc.drawString(timer_str, dimH-10, 0, GraphicsContext.RIGHT);
	}
	
	void generateFruits(){
		if((periodCounter % modulo) == 0 && fruits.size() < 8 && second_counter > 0){
			// on change la valeur du modulo pour que les fruits
			// apparaissent de façon irrégulière
			modulo = rdm.nextInt(33) + 10;
			try{
				fruits.add(new Fruit(dimH, dimV));
			}catch(IOException e){
				throw new AssertionError(e);
			}
		}
		periodCounter++;
	}
	
	void updateFruits(GraphicsContext gc){
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
				}
			}
		}
		click = click ? false : click;
		fruits.addAll(parts);
	}

	@Override
	public EventHandler getController() {
		return this;
	}

}
