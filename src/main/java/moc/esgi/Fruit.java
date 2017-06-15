package moc.esgi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import ej.microui.display.Image;

public class Fruit extends Body {
	
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
	
	//private static final Pattern r = Pattern.compile("/images/Fruits/(.*).png");
	private static String extractName(String path){
		return path.substring(path.lastIndexOf('/')+1, path.lastIndexOf('.'));
	}
	
	private final static Random rdm = new Random();
	public final static FruitDescriptor[] fruits = new FruitDescriptor[] {
			new FruitDescriptor("/images/Fruits/Banana_xs.png", 10),
			new FruitDescriptor("/images/Fruits/Coconut_xs.png", 10),
			new FruitDescriptor("/images/Fruits/Green_Apple_xs.png", 10),
			new FruitDescriptor("/images/Fruits/Kiwi_Fruit_xs.png", 10),
			new FruitDescriptor("/images/Fruits/Lemon_xs.png", 10),
			new FruitDescriptor("/images/Fruits/Lime_xs.png", 10),
			new FruitDescriptor("/images/Fruits/Mango_xs.png", 10),
//			new FruitDescriptor("/images/Fruits/Orange_xs.png", 10),
//			new FruitDescriptor("/images/Fruits/PassionFruit_xs.png", 10),
//			new FruitDescriptor("/images/Fruits/Peach_xs.png", 10),
//			new FruitDescriptor("/images/Fruits/Pineapple_xs.png", 10),
//			new FruitDescriptor("/images/Fruits/Plum_xs.png", 10),
//			new FruitDescriptor("/images/Fruits/Red_Apple_xs.png", 10),
			new FruitDescriptor("/images/Fruits/Strawberry_xs.png", 10),
			new FruitDescriptor("/images/Fruits/Tomato_xs.png", 10),
//			new FruitDescriptor("/images/Fruits/Watermelon_xs.png", 10)
	};
	
	public final static FruitDescriptor[] fruitParts = new FruitDescriptor[] {
			new FruitDescriptor("/images/Fruits/Tomato_xs_L.png", 0),
			new FruitDescriptor("/images/Fruits/Tomato_xs_R.png", 0),
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
			Fruit pR = new Fruit(
					FruitDescriptor.createImage("/images/Fruits/"+this.name+"_R.png"), 
					this.pos.clone(), 
					this.velocity.clone(), 
					0
			);
			pR.isPart = true;
			pR.dimH = this.dimH;
			pR.dimV = this.dimV;
			pR.addForce(new Push(new float[]{500, -50, 250}));
			parts.add(pR);
			
			Fruit pL = new Fruit(
					FruitDescriptor.createImage("/images/Fruits/"+this.name+"_L.png"), 
					this.pos.clone(), 
					this.velocity.clone(), 
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
	
	@Override
	public String toString() {
		return "Fruit [name=" + name + ", pos=" + Arrays.toString(pos) + ", velocity=" + Arrays.toString(velocity)
				+ "]";
	}
	
	
}
