
public class LoadTracker {
	private int slaveALoad;
	private int slaveBLoad;
	
	public LoadTracker() {
		slaveALoad = 0;
		slaveBLoad = 0;
	}
	
	public void addWorkA(int amount) {
		slaveALoad += amount;
	}
	
	public void removeWorkA(int amount) {
		slaveALoad -= amount;
	}
	
	public void addWorkB(int amount) {
		slaveBLoad += amount;
	}
	
	public void removeWorkB(int amount) {
		slaveBLoad -= amount;
	}
	
	public int getSlaveALoad() {
		return slaveALoad;
	}
	
	public int getSlaveBLoad() {
		return slaveBLoad;
	}
}
