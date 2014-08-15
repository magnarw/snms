package no.snms.app.alarm;

public class Alarm {
	
	public String getPrey() {
		return prey;
	}

	public void setPrey(String prey) {
		this.prey = prey;
	}
	
	public void setId(int id){
		this.Id = id;
	}
	
	public int getId(){
		return Id;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	String prey; 
	
	int offset;
	int Id;
	
	public Alarm(String p, int o, int id) {
		this.prey = p; 
		this.offset = o;
		this.Id = id;
	}
}
