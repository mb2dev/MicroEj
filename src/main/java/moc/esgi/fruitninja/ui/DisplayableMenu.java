package moc.esgi.fruitninja.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

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
import ej.widget.navigation.transition.HorizontalTransitionManager;
import moc.esgi.fruitninja.MainActivity;
import moc.esgi.fruitninja.models.Position;


public class DisplayableMenu  extends Widget implements EventHandler, Element {
	
	private final Image fruitNinjaBackground;
	private final Image scoreMelon;
	private final Image strawberry,watermelon,bomb;
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
	
	
	
	public DisplayableMenu(){
		super();
		try{
			strawberry = Image.createImage("/images/Fruits/Strawberry_xs.png");
			scoreMelon = Image.createImage("/images/Fruits/Menu/score_melon_xxs.png");
			watermelon = Image.createImage("/images/Fruits/Watermelon_xs.png");
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
		rotation.drawNearestNeighbor(gc, strawberry, strawberryPosition.getX(), strawberryPosition.getY(), GraphicsContext.HCENTER | GraphicsContext.VCENTER);
		gc.drawString("NEW GAME",160,240, GraphicsContext.RIGHT);
		rotation.setRotationCenter(watermelonPosition.getX(), watermelonPosition.getY());
		rotation.drawNearestNeighbor(gc, watermelon, watermelonPosition.getX(), watermelonPosition.getY(), GraphicsContext.HCENTER | GraphicsContext.VCENTER);
		gc.drawString("SCORE",290,140, GraphicsContext.RIGHT);
		rotation.setRotationCenter( bombPosition.getX(), bombPosition.getY());
		rotation.drawNearestNeighbor(gc, bomb, bombPosition.getX(), bombPosition.getY(), GraphicsContext.HCENTER | GraphicsContext.VCENTER);
		gc.drawString("EXIT",450,240, GraphicsContext.RIGHT);

		if(rotationValue%10==0){
			 Begin = circle( 100, 200, 60,Begin);
		}
		
		/*AntiAliasedShapes at = new AntiAliasedShapes();
		at.setThickness(15);
		at.drawCircleArc(gc, 60,160,90, 360, 360);*/
	
		
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
	

	
	public Position circle(float center_x, float center_y, float radius, Position p){
	
		float s = (float) Math.sin(radius);
		float c = (float) Math.cos(radius);
		
		// translate point back to origin:
		  p.addToX(-center_x);
		  p.addToY(-center_y);
		  // rotate point
		  float xnew = p.getX() * c + p.getY() * s;
		  float ynew = -p.getY() * s + p.getY() * c;

		  // translate point back:
		  p.setX((int) (xnew + center_x));
		  p.setY((int) (ynew + center_y));
		  return p;
		
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
			pX=list.get(i).getX();
			pY = list.get(i).getY();
			double resExit = Math.sqrt(Math.pow((pY-bombPosition.getY()),2) + Math.pow((pX - bombPosition.getX()),2));
			double resNewGame = Math.sqrt(Math.pow((pY-strawberryPosition.getY()),2) + Math.pow((pX-strawberryPosition.getX()),2));
			double resScore = Math.sqrt(Math.pow((pY-watermelonPosition.getY()),2) + Math.pow((pX-watermelonPosition.getX()),2));
				
			if(resExit<=10 && !transition){
				transition = true;
				if(resExit<=10){
					ExitHandler exitHandler = ServiceLoaderFactory.getServiceLoader().getService(ExitHandler.class); 
					if (exitHandler != null)
					{
					       exitHandler.exit();
					}
					break;
				}
			}
			else if(resNewGame<=10 && !transition){
				transition = true;
				MainActivity.nav.show(MainPage.class.getName(), false);
				break;
			}
			else if (resScore<=10 && !transition){
				transition = true;
				MainActivity.nav.setTransitionManager(new HorizontalTransitionManager());
				MainActivity.nav.show(ScorePage.class.getName(), false);
			}
			
		}
	}
	
	


	@Override
	public void render(GraphicsContext gc) {
		int width = this.getWidth();
		int height =this.getHeight();
		int[] centerPoint = { width / 2, height / 2 };
		// Counter diameter equals 9/10 of minimum widget dimension.
		int counterDiameter = (int) (Math.min(width, height) * 0.9);
		int circleDiameter = counterDiameter / 3;
		if(rotationValue >=360) {
			rotationValue =0;
		}else{
			rotationValue += 0.5;
		}
		//System.out.println(rotationValue);
		
		gc.drawImage(fruitNinjaBackground, 0, 0, 0);
		drawnFruit(gc);
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
