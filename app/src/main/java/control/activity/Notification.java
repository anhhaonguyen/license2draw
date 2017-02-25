package control.activity;

public interface Notification {

	public void notifyMessage(String message);
	public void notifyError();
	public void notifyConnected();
	public void notifyDisconnected();
}
