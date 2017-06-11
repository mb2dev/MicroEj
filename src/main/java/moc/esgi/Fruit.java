package moc.esgi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import ej.microui.display.Image;

public class Fruit extends Body {
	
	Image img;
	//Position pos;
	String name;
	int value;
	int dimH;
	int dimV;
	boolean fall = false;
	
	//private static final Pattern r = Pattern.compile("/images/Fruits/(.*).png");
	private static String extractName(String path){
		return path.substring(path.lastIndexOf('/')+1, path.lastIndexOf('.'));
	}
	
	private final static Random rdm = new Random();
	public final static FruitDescriptor[] fruits = new FruitDescriptor[] {
			new FruitDescriptor("/images/Fruits/Banana.png", 10),
			new FruitDescriptor("/images/Fruits/Coconut.png", 10),
			new FruitDescriptor("/images/Fruits/Green_Apple.png", 10),
			new FruitDescriptor("/images/Fruits/Kiwi_Fruit.png", 10),
			new FruitDescriptor("/images/Fruits/Lemon.png", 10),
			new FruitDescriptor("/images/Fruits/Lime.png", 10),
			new FruitDescriptor("/images/Fruits/Mango.png", 10),
			new FruitDescriptor("/images/Fruits/Orange.png", 10),
			new FruitDescriptor("/images/Fruits/PassionFruit.png", 10),
			new FruitDescriptor("/images/Fruits/Peach.png", 10),
			new FruitDescriptor("/images/Fruits/Pineapple.png", 10),
			new FruitDescriptor("/images/Fruits/Plum.png", 10),
			new FruitDescriptor("/images/Fruits/Red_Apple.png", 10),
			new FruitDescriptor("/images/Fruits/Strawberry.png", 10),
			new FruitDescriptor("/images/Fruits/Tomato.png", 10),
			new FruitDescriptor("/images/Fruits/Watermelon.png", 10)
	};
	
	public final static float[][] physics = new float[][] {
			{0.16f,600,-2800,88},
			{0.83f,-600,-2600,100},
			{0.55f,-100,-2600,367},
			{0.45f,100,-2700,250}
	};

	public Fruit(Image img, float[] pos,/*Position position,*/ int value) {
		super(new float[]{0,0,0}, new float[]{1,0,0}, new float[][]{
				{1,0,0},
				{0,1,0},
				{0,0,1}
		});
		this.img = img;
		//this.pos = pos;
		this.value = value;
	}

	public Fruit(Image img, int value) {
		this(img, new float[]{-100,-100,0},/*new Position(-100, -100),*/ value);
	}
	
	public Fruit(int dimH, int dimV) throws IOException {
		FruitDescriptor desc = fruits[rdm.nextInt(fruits.length)];
		//this(FruitDescriptor.createImage(desc.imgPath), new float[]{-100,-100,0}, desc.value);
		this.name = extractName(desc.imgPath);
		this.value = desc.value;
		this.img = FruitDescriptor.createImage(desc.imgPath);
//		super(new float[]{(dimH / 2) - img.getWidth() / 2, dimV + img.getHeight(), 0}, new float[]{1,0,0}, new float[][]{
//			{1,0,0},
//			{0,1,0},
//			{0,0,1}
//	});
		float[] phys = physics[rdm.nextInt(physics.length)];
		this.pos = new float[]{(dimH * phys[0]), dimV + img.getHeight(), 0};
		this.velocity = new float[]{0,-1,0};
		this.inertia = new float[][]{
			{1,0,0},
			{0,1,0},
			{0,0,1}
		};
		this.forces = new ArrayList<Force>();
		this.forces.add(new Push(new float[]{phys[1],phys[2],phys[3]}));
		//this.forces.add(new Spring(10.f, 1.f, 0, 0, 1));
		this.dimH = dimH;
		this.dimV = dimV;
	}
	
	/**
	 * Acceleration calculation : https://stackoverflow.com/questions/22865822/move-an-object-based-on-acceleration
	 */
	public void update(){
		/*if(!this.fall){
			this.fall = this.position.Y < (dimV / 6);
		}
		if(fall)
			this.position.Y += 10;
		else
			this.position.Y -= 10;*/
		integrate();
	}
	
	public void breakFruit(){
		// TODO
	}
	
	public boolean toDelete(){
//		return (this.pos.Y - img.getHeight()) > dimV 
//				|| (this.pos.Y + img.getHeight()) < 0
//				|| (this.pos.X + img.getWidth()) < 0
//				|| (this.pos.X - img.getWidth()) > dimH;
		return (this.pos[1] - img.getHeight()) > (dimV+100) 
				|| (this.pos[1] + img.getHeight()) < 0
				|| (this.pos[0] + img.getWidth()) < 0
				|| (this.pos[0] - img.getWidth()) > (dimV+100);
//		return false;
	}

	
	@Override
	public String toString() {
		return "Fruit [name=" + name + ", pos=" + Arrays.toString(pos) + ", velocity=" + Arrays.toString(velocity)
				+ "]";
	}
}
