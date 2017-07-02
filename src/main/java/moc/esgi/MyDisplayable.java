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
import ej.microui.display.shape.AntiAliasedShapes;
import ej.microui.display.transform.ImageRotation;
import ej.microui.event.Event;
import ej.microui.event.generator.Pointer;
import ej.microui.util.EventHandler;
import ej.mwt.Widget;
import ej.style.Element;
import ej.style.State;

public class MyDisplayable extends Widget implements EventHandler, Element {

	private final static Random rdm = new Random();
	private final static int GAME_LENGTH = 30;
	private final static int DELAY = 3;
	
	int dimH;
	int dimV;
	
	boolean dragging = false;
	
	int pY = 0;
	int pX = 0;
	int periodCounter = 0;
	int modulo = 10;
	
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
	private ArrayList<Position> list = new ArrayList<Position>();
	boolean clearList = false;
	boolean goNext = false;
	Position pa;
	Position pb;
	
	public MyDisplayable() {
		super();
//		dimV = Display.getDefaultDisplay().getHeight();
//		dimH = Display.getDefaultDisplay().getWidth();
		try{
			scoreMelon = Image.createImage("/images/Fruits/Menu/score_melon_xxs.png");
			fruitNinjaBackground = Image.createImage("/images/fruit_background.png");
		}
		catch(IOException e){
			throw new AssertionError(e);
		}
		FruitDescriptor.InitFruitDescriptor(Fruit.fruits);
		FruitDescriptor.InitFruitDescriptor(Fruit.fruitParts);
		
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
			if(Pointer.isReleased(event)){
				clearList = true;
				dragging = false;
				return true;
			}
			
			if(Pointer.isPressed(event))
			{
				//System.out.println("click");
				if(isStopped()){
					second_counter = GAME_LENGTH;
					score = 0;
					resetTimer();
				}
				return true;
//				click = true;
//				Pointer ptr = (Pointer)Event.getGenerator(event);
//				pX = ptr.getX();
//				pY = ptr.getY();
//				repaint();
//				return true;
			}
			if(Pointer.isDoubleClicked(event)){
				//System.out.println("double click");
				if(isStopped()){
					second_counter = GAME_LENGTH;
					score = 0;
					resetTimer();
				}
				return true;
			}
			if(Pointer.isDragged(event))
			{
				 dragging = true;
				 Pointer ptr = (Pointer)Event.getGenerator(event);
				 pX = ptr.getX();
				 pY = ptr.getY();
				 Position p = new Position(pX, pY);
				 int size = list.size();
				 if(size > 0 && !p.equals(list.get(size-1))){
					 list.add(p);
				 }else if(size == 0){
					 list.add(p);
				 }
				 if(size > 10){
					 list.remove(0);
				 }
				 //list.add(p);
				 //repaint();
				 return true;
			}
			
		}
		return false;
	}

	public boolean isStopped(){
		return second_counter <= -DELAY;
	}
	
	void drawCut(GraphicsContext gc){
		if(clearList){
			list.clear();
			clearList = false;
		}
		AntiAliasedShapes at = new AntiAliasedShapes();
		for(int i=0;i< list.size()-1;i++){
			pa = list.get(i);
			pb = list.get(i+1);
			at.setFade(10);
			at.setThickness(0);
			gc.setColor(Colors.CYAN);
			at.drawLine(gc, list.get(i).getX(),list.get(i).getY(),  list.get(i+1).getX(), list.get(i+1).getY());
			gc.setColor(Colors.WHITE);
			at.setFade(2);
			at.setThickness(4);
			at.drawLine(gc, list.get(i).getX(),list.get(i).getY(),  list.get(i+1).getX(), list.get(i+1).getY());
//			gc.drawLine(pa.getX(), pa.getY(), pb.getX(), pb.getY());
		}
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
		if((periodCounter % modulo) == 0 && fruits.size() < 3 && second_counter > 0){
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
				//System.out.println(f.pa.toString());
			 if(f.intersect(list) && !f.isPart){
				  
					i.remove();
					score += f.value;
				
					/**
					 * /!\ ConcurrentModificationException
					 */
					/*labels.add(
							new FNPoints(
									new String("+"+f.value), 
									new Position(f.getX(), f.getY())
							)
					);*/
					parts = f.breakFruit();
				}
			 
			 if(!dragging)
				{
					if(f.toDelete()){
						//System.out.println("deleting "+f.toString());
						i.remove();
					}
				}
				
			 f.update();	
		}
		
		fruits.addAll(parts);
		
	}

//	@Override
//	public void render(GraphicsContext g) {
//		paint(g);
//	}

	@Override
	public void validate(int widthHint, int heightHint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		super.setBounds(x, y, width, height);
		//System.out.println("setBounds");
		dimV = height;
		dimH = width;
	}

	@Override
	public boolean hasClassSelector(String classSelector) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInState(State state) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getAttribute(String attribute) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element getParentElement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element[] getChildrenElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element getChild(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getChildrenCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void render(GraphicsContext gc) {
		// TODO Auto-generated method stub
		gc.drawImage(fruitNinjaBackground, 0, 0, 0);
		gc.drawImage(scoreMelon, 0, 0, 0);
		gc.setColor(0xfae74c);
		gc.setFont(font);
		
		if(!isStopped()){
			updateFruits(gc);
			generateFruits();
			
			drawFruits(gc);
			drawPoints(gc);
			drawCut(gc);
		}else{
			fruits.clear();
			gc.drawString("SCORE : "+score, dimH/2, dimV/2, GraphicsContext.HCENTER | GraphicsContext.VCENTER);
			if(!goNext){
				Score.sharedScore.addScore(score);
				goNext = true;
				MainActivity.nav.show(MenuPage.class.getName(), false);
			}
		}
		updateInfo(gc);
	}
	

}
