//Kevin Gilboy
//This interface guarantees time controls
//Was originally developed for independent CTC usage

package Modules.Ctc;

public interface TimeControl {
	public void pause();
	public void play();
	public void setSpeedup(int newSpeed);
}
