package moc.esgi;

public class Spring implements Force {

	float stiffness;
	float damping;
	float restLength;
	float restPos;
	int coordIdx;
	
	
	
	public Spring(float stiffness, float damping, float restLength, float restPos, int coordIdx) {
		super();
		this.stiffness = stiffness;
		this.damping = damping;
		this.restLength = restLength;
		this.restPos = restPos;
		this.coordIdx = coordIdx;
		
		System.out.println(this);
	}



	@Override
	public float[] applyForce(float[] pos, float[] velocity) {
		float[] f = new float[]{0,0,0};
		float value = - stiffness * ((pos[coordIdx] - restLength) - restPos) - damping * velocity[coordIdx];
		f[coordIdx] = value;
		
		//System.out.println(- stiffness * ((pos[coordIdx] - restLength) - restPos));
		//System.out.println(- damping * velocity[coordIdx]);
		//System.out.println("f[coordIdx] = "+value);
		return f;
	}



	@Override
	public String toString() {
		return "Spring [stiffness=" + stiffness + ", damping=" + damping + ", restLength=" + restLength + ", restPos="
				+ restPos + ", coordIdx=" + coordIdx + "]";
	}
	
	

}
