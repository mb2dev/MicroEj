package moc.esgi;

import java.util.ArrayList;
import java.util.Collections;

import ej.widget.container.List;

public class Score {
	
	
	public static Score sharedScore = new Score();
	private ArrayList<Integer> scoreList;
	
	public Score(){
		this.scoreList = new ArrayList<Integer>();
	}
	
	public void addScore(int score){
		if(scoreList.size()<3){
			scoreList.add(score);
		}
		else if(score > scoreList.get(scoreList.size()-1)){
		 scoreList.remove(scoreList.size()-1);
		 scoreList.add(score);
		}
		Collections.sort(scoreList);
		Collections.reverse(scoreList);
		
	}
	
	public ArrayList<Integer> getScore(){
		return this.scoreList;
	}
	

}
