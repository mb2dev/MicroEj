package moc.esgi.fruitninja.models;

public class FNPoints {
	String label;
	int lifecounter = 0;
	int lifespan = 10;
	Position pos;
	
	public FNPoints(String label, Position pos) {
		super();
		this.label = label;
		this.pos = pos;
	}
	
	public String display(){ lifecounter++; return label; }
	public boolean toDelete(){ return lifecounter >= lifespan; }

	public Position getPos() {
		return pos;
	} 
	
}
