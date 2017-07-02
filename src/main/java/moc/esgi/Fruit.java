package moc.esgi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import ej.microui.display.Image;

public class Fruit extends Body {
	
	private final static int MARGE = 20;
	
	Image img;
	String name;
	int value;
	boolean isPart = false;
	
	//Object dimensions
	/*int height;
	int width;*/
	
	// Scene dimensions
	int dimH;
	int dimV;
	
	Position pa;
	Position pb;
	
	//private static final Pattern r = Pattern.compile("/images/Fruits/(.*).png");
	private static String extractName(String path){
		return path.substring(path.lastIndexOf('/')+1, path.lastIndexOf('.'));
	}
	
	private final static Random rdm = new Random();
	public final static FruitDescriptor[] fruits = new FruitDescriptor[] {
			new FruitDescriptor("/images/Fruits/Banana_xs.png", 25),
			new FruitDescriptor("/images/Fruits/Coconut_xs.png", 10),
			/*new FruitDescriptor("/images/Fruits/Green_Apple_xs.png", 15),
			new FruitDescriptor("/images/Fruits/Kiwi_Fruit_xs.png", 5),
			new FruitDescriptor("/images/Fruits/Lemon_xs.png", 12),
			new FruitDescriptor("/images/Fruits/Lime_xs.png", 8),
			new FruitDescriptor("/images/Fruits/Mango_xs.png", 4),*/
//			new FruitDescriptor("/images/Fruits/Orange_xs.png", 10),
//			new FruitDescriptor("/images/Fruits/PassionFruit_xs.png", 10),
//			new FruitDescriptor("/images/Fruits/Peach_xs.png", 10),
//			new FruitDescriptor("/images/Fruits/Pineapple_xs.png", 10),
//			new FruitDescriptor("/images/Fruits/Plum_xs.png", 10),
//			new FruitDescriptor("/images/Fruits/Red_Apple_xs.png", 10),
			new FruitDescriptor("/images/Fruits/Strawberry_xs.png", 40),
		    new FruitDescriptor("/images/Fruits/Tomato_xs.png", 32),
//			new FruitDescriptor("/images/Fruits/Watermelon_xs.png", 10)
	};
	
	public final static FruitDescriptor[] fruitParts = new FruitDescriptor[] {
			new FruitDescriptor("/images/Fruits/Tomato_xs_L.png", 0),
			new FruitDescriptor("/images/Fruits/Tomato_xs_R.png", 0),
			new FruitDescriptor("/images/Fruits/Banana_xs_L.png", 0),
			new FruitDescriptor("/images/Fruits/Banana_xs_R.png", 0),
			/*new FruitDescriptor("/images/Fruits/Coconut_xs_L.png", 0),
			new FruitDescriptor("/images/Fruits/Coconut_xs_R.png", 0),
			new FruitDescriptor("/images/Fruits/Green_Apple_xs_L.png", 0),
			new FruitDescriptor("/images/Fruits/Green_Apple_xs_R.png", 0),
			new FruitDescriptor("/images/Fruits/Kiwi_Fruit_xs_L.png", 0),
			new FruitDescriptor("/images/Fruits/Kiwi_Fruit_xs_R.png", 0),*/
			/*new FruitDescriptor("/images/Fruits/Lemon_xs_L.png", 0),
			new FruitDescriptor("/images/Fruits/Lemon_xs_R.png", 0),
			new FruitDescriptor("/images/Fruits/Lime_xs_L.png", 0),
			new FruitDescriptor("/images/Fruits/Lime_xs_R.png", 0),
			new FruitDescriptor("/images/Fruits/Mango_xs_L.png", 0),
			new FruitDescriptor("/images/Fruits/Mango_xs_R.png", 0),*/
			new FruitDescriptor("/images/Fruits/Strawberry_xs_L.png", 0),
			new FruitDescriptor("/images/Fruits/Strawberry_xs_R.png", 0),
			
	};
	
	public final static float[][] physics = new float[][] {
			{0.16f,600,-2300,88},
			{0.83f,-600,-2200,100},
			{0.55f,-100,-2150,367},
			{0.45f,100,-2000,250}
	};

	public Fruit(Image img, float[] pos, float[] velocity, int value) {
		super(pos, velocity, new float[][]{
				{1,0,0},
				{0,1,0},
				{0,0,1}
		});
		this.img = img;
		this.value = value;
	}
	
	public Fruit(int dimH, int dimV,Image img){
		this.dimH = dimH;
		this.dimV = dimV;
		this.img = img;
	}
	
	public Fruit(int dimH, int dimV) throws IOException {
		FruitDescriptor desc = fruits[rdm.nextInt(fruits.length)];
		
		this.name = extractName(desc.imgPath);
		this.value = desc.value;
		this.img = FruitDescriptor.createImage(desc.imgPath);

		float[] phys = physics[rdm.nextInt(physics.length)];
		this.pos = new float[]{(dimH * phys[0]), dimV + img.getHeight(), 0};
		this.velocity = new float[]{0,-1,0};
		this.inertia = new float[][]{
			{1,0,0},
			{0,1,0},
			{0,0,1}
		};
		this.forces = new ArrayList<Force>();
		this.forces.add(new Push(new float[]{phys[1],(rdm.nextInt(850)+2000)*-1, rdm.nextInt(1000)-500}));
		
		this.dimH = dimH;
		this.dimV = dimV;
	}
	
	/**
	 * Acceleration calculation : https://stackoverflow.com/questions/22865822/move-an-object-based-on-acceleration
	 */
	public void update(){
		integrate();
	}
	
	public ArrayList<Fruit> breakFruit() {
		ArrayList<Fruit> parts = new ArrayList<Fruit>(2);
		if(isPart){
			return parts;
		}
		
		try{
			float[] pos_clone = new float[]{
				this.pos[0],
				this.pos[1],
				this.pos[2]
			};
			float[] vel_clone = new float[]{
					this.velocity[0],
					this.velocity[1],
					this.velocity[2]
			};
			Fruit pR = new Fruit(
					FruitDescriptor.createImage("/images/Fruits/"+this.name+"_R.png"), 
					pos_clone, 
					vel_clone, 
					0
			);
			pR.isPart = true;
			pR.dimH = this.dimH;
			pR.dimV = this.dimV;
			pR.addForce(new Push(new float[]{500, -50, 250}));
			parts.add(pR);
			
			pos_clone = new float[]{
				this.pos[0],
				this.pos[1],
				this.pos[2]
			};
			vel_clone = new float[]{
					this.velocity[0],
					this.velocity[1],
					this.velocity[2]
			};
			Fruit pL = new Fruit(
					FruitDescriptor.createImage("/images/Fruits/"+this.name+"_L.png"), 
					pos_clone, 
					vel_clone, 
					0
			);
			pL.isPart = true;
			pL.dimH = this.dimH;
			pL.dimV = this.dimV;
			pL.addForce(new Push(new float[]{-500, -50,-250}));
			parts.add(pL);
		}catch(IOException e){
			e.printStackTrace();
			//throw new AssertionError(e);
		}
		return parts;
	}
	
	public boolean toDelete(){	
		return (this.pos[1] - img.getHeight()) > (dimV+100) 
				|| (this.pos[1] + img.getHeight()) < 0
				|| (this.pos[0] + img.getWidth()) < 0
				|| (this.pos[0] - img.getWidth()) > (dimV+100);
	}
	
	public int getWidth(){ return this.img.getWidth(); }
	public int getHeight(){ return this.img.getHeight(); }
	public int getX(){ return (int)this.pos[0]; }
	public int getY(){ return (int)this.pos[1]; }

	public boolean intersect(int x, int y){
		return x < this.pos[0] + this.getWidth()/2 
				&& x > this.pos[0] - this.getWidth()/2
				&& y < this.pos[1] + this.getHeight()/2
				&& y > this.pos[1] - this.getHeight()/2;
	}
	
	public boolean intersect(ArrayList<Position> list){
		//System.out.println("list length =" +list.size() );
		for(int i = 0; i < list.size()-1; i++){
			int m_y = (int)this.pos[1];
			int m_x = (int)this.pos[0];
			
			pa = list.get(i);
			pb = list.get(i+1);
			int x_left = (pa.X > pb.X ? pb.X : pa.X) - MARGE;
		    int x_right = (pa.X > pb.X ? pa.X : pb.X) + MARGE;
			int y_top = (pa.Y > pb.Y ? pb.Y : pa.Y) - MARGE;
			int y_bottom = (pa.Y > pb.Y ? pa.Y : pb.Y) + MARGE;
			
			if(m_x >= x_left && m_x <= x_right && m_y >= y_top && m_y < y_bottom){
				//System.out.println("inbox");
//				double a = (double)pb.X / (double)pa.X;
//				double b = (double)pb.Y / (double)pa.Y;
//				double y = a*this.pos[0]+b;
//				//System.out.println(y + " == " + m_y + " ?");
//				if(Math.abs(m_y - y) < 50){
//					return true;
//				}
				return true;
			}
		}
		return false; 
	}
	
	@Override
	public String toString() {
		return "Fruit [name=" + name + ", pos=" + Arrays.toString(pos) + ", velocity=" + Arrays.toString(velocity)
				+ "]";
	}
	
	
}
