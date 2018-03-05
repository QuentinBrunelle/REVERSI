import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class Reversi extends Application {	
	public static GridWindow gridWindow;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		gridWindow = new GridWindow(primaryStage);
		
		GameLauncher gameLauncher = new GameLauncher();
		gameLauncher.valueProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null) {
				if (newValue.action == 0)
					Reversi.gridWindow.setGrid(newValue.x, newValue.y, 1000);
				else if (newValue.action == 1)
					Reversi.gridWindow.drawSquare(newValue.x, newValue.y, newValue.color);
				else if (newValue.action == 2)
					Reversi.gridWindow.writeCenterCrimsonGold(newValue.str, 75);
				Reversi.gridWindow.notifyAllThreadLock();
			}
		});		
		gameLauncher.start();
		
	}
		
	public static void main(String[] args) {
		launch(args);
	}

}

class GameLauncher extends Service<ValueFromThread> {
	@Override
	protected Task<ValueFromThread> createTask() {
		TaskGame task = new TaskGame(); 
		
		return task;
	}

}

class TaskGame extends Task<ValueFromThread> {	
	
	public TaskGame() {
		setOnSucceeded(new TaskSucceedHandler());
		this.setOnFailed(new TaskFailedHandler(this));
	}
	
	@Override
	protected ValueFromThread call() throws Exception {
		new Game(this).launch();
		return null;
	}	
	
	public void drawGrid(int sizeAbs, int sizeOrd) {
		updateValue(new ValueFromThread(0, sizeAbs, sizeOrd, null, null));
		synchronized (Reversi.gridWindow.getThreadLock()) {
			try {
				Reversi.gridWindow.getThreadLock().wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void drawSquare(int x, int y, Paint color) {
		Reversi.gridWindow.drawSquare(x, y, color);
//		updateValue(new ValueFromThread(1, x, y, color, null));	
//		synchronized (Reversi.gridWindow.getThreadLock()) {
//			try {
//				Reversi.gridWindow.getThreadLock().wait();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	public void drawRedSquare(int x, int y) {
		drawSquare(x, y, Color.RED);
	}
	
	public void drawBlueSquare(int x, int y) {
		drawSquare(x, y, Color.BLUE);
	}
	
	public void drawGreenSquare(int x, int y) {
		drawSquare(x, y, Color.GREEN);
	}
	
	public void drawWhiteSquare(int x, int y) {
		drawSquare(x, y, Color.WHITE);
	}
	
	public void waitForMouseClick() {
		synchronized (Reversi.gridWindow.getMouseLock()) {
			try {
				Reversi.gridWindow.getMouseLock().wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getX() { return Reversi.gridWindow.getX(); }
	
	public int getY() { return Reversi.gridWindow.getY(); }
}

class ValueFromThread{
	int action;
	int x;
	int y;
	Paint color;
	String str;
	
	public ValueFromThread(int action, int x, int y, Paint color, String str) {
		this.action = action;
		this.x = x;
		this.y = y;
		this.color = color;
		this.str = str;
	}
}

class TaskSucceedHandler implements EventHandler<WorkerStateEvent> {	
	@Override
	public void handle(WorkerStateEvent event) {
		System.exit(0);
	}
	
}

class TaskFailedHandler implements EventHandler<WorkerStateEvent> {
	private TaskGame t;
	
	public TaskFailedHandler(TaskGame t) {
		this.t = t;
	}

	@Override
	public void handle(WorkerStateEvent event) {		
		t.getException().printStackTrace(System.err);
		
		System.exit(1);
	}
	
}
