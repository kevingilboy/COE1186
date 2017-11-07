package Modules.TrackModel;

public class Beacon{

	private int info_32bit;
	
	public Beacon(){
		info_32bit = 0;
	}

	public int getInfo(){
		return info_32bit;
	}

	public void setInfo(int info_32bit){
		this.info_32bit = info_32bit;
	}
}