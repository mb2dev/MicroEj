package moc.esgi;

import java.io.IOException;
import java.util.HashMap;

import ej.microui.display.Image;

public class FruitDescriptor {
	int value;
	String imgPath;
	
	static HashMap<String, Image> images = new HashMap<String, Image>();
	
	static Image createImage(String name) throws IOException {
		Image img = images.get(name);
		if(img == null){
			//System.out.println("create");
			img = Image.createImage(name);
			images.put(name, img);
		}else{
			//System.out.println("recycling image");
		}
		return img;
	}
	
	public FruitDescriptor(String imgPath, int value) {
		super();
		this.value = value;
		this.imgPath = imgPath;
	}
	
	static void InitFruitDescriptor(FruitDescriptor[] fruitList){
		for(int i =0; i< fruitList.length; i++){
			Image img;
			try {
				img = Image.createImage(fruitList[i].imgPath);
				images.put(fruitList[i].imgPath,img);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	@Override
	public String toString(){
		return "Fruit Descriptor { path = "+ imgPath +" }";
	}
}
