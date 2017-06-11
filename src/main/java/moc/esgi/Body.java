package moc.esgi;

import java.util.ArrayList;
import java.util.Arrays;

public class Body {
	
	private final static float dt = 0.1f; //ms
	
	float[] pos;
	float[] velocity;
	float[][] inertia;
	ArrayList<Force> forces;
	
	public Body(float[] pos, float[] velocity, float[][] inertia){
		super();
		this.pos = pos;
		this.velocity = velocity;
		this.inertia = inertia;
		this.forces = new ArrayList<Force>();
	}
	
	public Body(){
		this(new float[]{0,0,0}, new float[]{1,0,0}, new float[][]{
				{1,0,0},
				{0,1,0},
				{0,0,1}
		});
	}
	
	public void integrate(){
		float[] a = forceAcceleration();
		float[] g = gravityAcceleration();
		//System.out.println("forceAcceleration = "+ Arrays.toString(a));
		for(int i = 0; i < a.length; i++){
			this.velocity[i] += g[i];
			this.velocity[i] += a[i] * dt;
		}
		//this.velocity = this.velocity + a * dt;
		this.pos[0] = this.pos[0] + this.velocity[0] * dt;
		this.pos[1] = this.pos[1] + this.velocity[1] * dt;
		this.pos[2] = this.pos[2] + this.velocity[2] * dt;
		//System.out.println(this.toString());
	}
	
	public float[] forceAcceleration(){
		float[] f = new float[]{0,0,0};
		for(Force force : forces){
			float[] tmp_f = force.applyForce(this.pos, this.velocity);
			//System.out.println("tmp_f after applyForce = "+ Arrays.toString(tmp_f));
			for(int i = 0; i < tmp_f.length; i++){
				f[i] += tmp_f[i];
			}
		}
		
		float[] a = new float[]{0,0,0};
		// http://mathinsight.org/matrix_vector_multiplication
		for(int i = 0; i < f.length; i++){
			for(int j = 0; j < f.length; j++){
				a[i] += f[j] * this.inertia[i][j]; 
			}
		}
		return a; // inv(M) * f
	} // forceAcceleration
	
	public float[] gravityAcceleration(){
		float[] a = new float[]{ 0, 9.81f, 0 };
		// Julia
//		for i=1:length(state.pos)/3
//				a = append!(a, [0, -9.81, 0.])
		return a;
	}
	
	public void addForce(Force f){
		forces.add(f);
	}
}
