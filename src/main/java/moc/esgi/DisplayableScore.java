package moc.esgi;

import java.io.IOException;
import java.util.ArrayList;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.exit.ExitHandler;
import ej.microui.display.Colors;
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


public class DisplayableScore  extends Widget implements EventHandler, Element {
	
	private final Image fruitNinjaBackground;
	private final Image bomb;
	public float rotationValue =0;
	private final Font font = Font.getFont(Font.LATIN, 26, Font.STYLE_PLAIN);
	float point_x, point_y;
	Position Begin = new Position(150,200);
	private ArrayList<Position> list = new ArrayList<Position>();
	boolean clearList = false;
	Position pa;
	Position pb;
	int pY = 0;
	int pX = 0;
	boolean dragging = false;
	boolean transition = false;
	Position strawberryPosition = new Position(100,200);
	Position watermelonPosition = new Position(250,100);
	Position bombPosition = new Position(420,190);
	int scoreX= 250;
	int scoreY =50;
	
	
	
	public DisplayableScore(){
		super();
		try{
			bomb =  Image.createImage("/images/Fruits/Bomb_xs.png");
			fruitNinjaBackground = Image.createImage("/images/fruit_background.png");
			
		}
		catch(IOException e){
			throw new AssertionError(e);
		}
	}
	
	public void drawnFruit(GraphicsContext gc){
		gc.setColor(0xfae74c);
		gc.setFont(font);
		
		ImageRotation rotation = new ImageRotation();
		rotation.setRotationCenter(100, 200);
		rotation.setAngle((int)rotationValue % 360);
		rotation.setRotationCenter( bombPosition.X, bombPosition.Y);
		rotation.drawNearestNeighbor(gc, bomb, bombPosition.X, bombPosition.Y, GraphicsContext.HCENTER | GraphicsContext.VCENTER);
		gc.drawString("BACK",450,240, GraphicsContext.RIGHT);	
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
	
	void drawnScore(GraphicsContext gc){
		
		gc.drawString("TOP 3",scoreX,scoreY, GraphicsContext.HCENTER | GraphicsContext.VCENTER);
		for(int i = 0; i<Score.sharedScore.getScore().size(); i++){
			gc.drawString(""+i,scoreX-50,(scoreY+(50*(i+1))), GraphicsContext.HCENTER | GraphicsContext.VCENTER);
			gc.drawString((""+Score.sharedScore.getScore().get(i)) ,scoreX+10,(scoreY+(50*(i+1))), GraphicsContext.HCENTER | GraphicsContext.VCENTER);
		}
	}
	


	
	@Override
	public boolean handleEvent(int event) {
		if(Event.getType(event) == Event.POINTER){
			if(Pointer.isReleased(event)){
				clearList = true;
				dragging = false;
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
	
	public void goNext(){
		for(int i = 0; i < list.size()-1; i++){
			pX=list.get(i).X;
			pY = list.get(i).Y;
			double resExit = Math.sqrt(Math.pow((pY-bombPosition.Y),2) + Math.pow((pX - bombPosition.X),2));
			double resNewGame = Math.sqrt(Math.pow((pY-strawberryPosition.Y),2) + Math.pow((pX-strawberryPosition.X),2));
			double resScore = Math.sqrt(Math.pow((pY-watermelonPosition.Y),2) + Math.pow((pX-watermelonPosition.X),2));
				
			if(resExit<=10 && !transition){
				transition = true;
				MainActivity.nav.show(MenuPage.class.getName(), false);
				
			}		
		}
	}
	
	


	@Override
	public void render(GraphicsContext gc) {
		if(rotationValue >=360) {
			rotationValue =0;
		}else{
			rotationValue += 0.5;
		}
		
		gc.drawImage(fruitNinjaBackground, 0, 0, 0);
		drawnFruit(gc);
		drawnScore(gc);
		drawCut(gc);
		goNext();
		repaint();
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
	public void validate(int widthHint, int heightHint) {
		// TODO Auto-generated method stub
		
	}

}
	
	