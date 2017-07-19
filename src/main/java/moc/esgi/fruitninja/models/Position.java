package moc.esgi.fruitninja.models;

public class Position {

	int X;
	int Y;
	public Position(int x, int y) {
		super();
		X = x;
		Y = y;
	}
	public int getX() {
		return X;
	}
	public void setX(int x) {
		X = x;
	}
	public int getY() {
		return Y;
	}
	public void setY(int y) {
		Y = y;
	}
	
	public void addToY(float v){
		Y += v;
	}
	
	public void addToX(float v){
		X += v;
	}
	
	@Override
	public String toString() {
		return "Position [X=" + X + ", Y=" + Y + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + X;
		result = prime * result + Y;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (X != other.X)
			return false;
		if (Y != other.Y)
			return false;
		return true;
	}
	
	
}
