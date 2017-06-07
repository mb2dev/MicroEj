package moc.esgi;

import java.io.IOException;
import java.util.ArrayList;

import ej.microui.display.Colors;
import ej.microui.display.Display;
import ej.microui.display.Displayable;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Image;
import ej.microui.display.shape.AntiAliasedShapes;
import ej.microui.event.Event;
import ej.microui.event.generator.Pointer;
import ej.microui.util.EventHandler;

public class Menu extends Displayable implements EventHandler {
	
	
	private Image myBackground;
	int pY = 0;
	int pX = 0;
	
	ArrayList<Position> list = new ArrayList<Position>();
	

	public Menu(Display display) {
		super(Display.getDefaultDisplay());
		// TODO Auto-generated constructor stub
		System.out.print("bonjour");
		try {
			myBackground = Image.createImage("/images/fruit_background.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}

	@Override
	public void paint(GraphicsContext g) {
		System.out.print("bonjour " +pX +""+pY);
		
		
		
		
		// TODO Auto-generated method stub
		//g.setColor(Colors.CYAN);
		//g.fillCircle(0, 0, 800/2);
		
		//g.drawImage(myBackground,  10, 10,0,0);
		g.drawImage(myBackground , 0,0,0);
		
		
		AntiAliasedShapes at = new  AntiAliasedShapes();
		g.setColor(Colors.RED);
		
		
		
		for(int i=0;i< list.size()-1;i++){
			at.setFade(10);
			at.setThickness(0);
			g.setColor(Colors.CYAN);
			at.drawLine(g, list.get(i).getX(),list.get(i).getY(),  list.get(i+1).getX(), list.get(i+1).getY());
			g.setColor(Colors.WHITE);
			at.setFade(2);
			at.setThickness(4);
			at.drawLine(g, list.get(i).getX(),list.get(i).getY(),  list.get(i+1).getX(), list.get(i+1).getY());
			
			/*if( dx > dx1 && dy==dy1) {
				while(dx !=dx1){
					at.drawLine(g, 50, 50, 300, 300);
					g.fillRoundRect(dx--, list.get(i).getY(),10,10, 10, 10);
				}	
			}*/
			
			/*if(dx > dx1 && dy > dy1){
				while(dx !=dx1 &&  dy!=dx1){
					g.fillRoundRect(dx--, dy--,10,10, 10, 10);
				}
			}
			
			if(dx > dx1 && dy < dy1){
				while(dx !=dx1 &&  dy!=dx1){
					g.fillRoundRect(dx--, dy++,10,10, 10, 10);
				}
			}*/
			
			
			/*if(dx < dx1  && dy==dy1){
				while(dx !=dx1){
					g.fillRoundRect(dx++, list.get(i).getY(),10,10, 10, 10);
				}
			}*/
			
			/*if(dx < dx1 && dy>dy1){
				while(dx !=dx1 &&  dy!=dx1){
					g.fillRoundRect(dx++, dy--,10,10, 10, 10);
				}
			}
			
			if(dx < dx1 && dy<dy1){
				while(dx !=dx1 &&  dy!=dx1){
					g.fillRoundRect(dx++, dy++,10,10, 10, 10);
				}
			}*/
			
			/*if(dx == dx1 && dy > dy1){
				while(dy !=dy1){
					
					g.fillRoundRect(list.get(i).getX(), dy--,10,10, 10, 10);
				}
			}
			
			if(dx == dx1 && dy < dy1){
				while(dy !=dy1){
					g.fillRoundRect(list.get(i).getX(), dy++,10,10, 10, 10);
				}
			}*/
			
			
		
			//i++;
			
	        
			//g.drawLine(list.get(i).getX(), list.get(i).getY(), list.get(i+1).getX(), list.get(i+1).getY());
			//g.fillCircle(list.get(i).getX()-5, list.get(i).getY()-5, 10);
			
		}
		
		repaint();
	
		
		
	}

	@Override
	public EventHandler getController() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public boolean handleEvent(int event) {
		if(Event.getType(event) == Event.POINTER){
		
			if(Pointer.isDragged(event))
			{
			
				//click = true;
				//bounce = false;
				
				 Pointer ptr = (Pointer)Event.getGenerator(event);
				 pX = ptr.getX();
				 pY = ptr.getY();
				 list.add(new Position(pX,pY));
		
			
			}
		}
		return false;

	}
	
}
