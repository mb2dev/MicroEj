package moc.esgi;

public class Push implements Force {

	float[] vector;
	boolean firstTime;
	
	public Push(float[] v){
		vector = v;
		firstTime = true;
	}
	
	@Override
	public float[] applyForce(float[] pos, float[] velocity) {
		if(firstTime){
			firstTime = false;
			return vector;
		}
		return new float[]{0, 0, 0};
	}

}
