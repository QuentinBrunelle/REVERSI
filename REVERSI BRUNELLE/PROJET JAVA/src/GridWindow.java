import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

class MouseClick implements EventHandler<MouseEvent> {
	private GridWindow gridWindow;
	
	public MouseClick(GridWindow gridWindow) {
		this.gridWindow = gridWindow;
	}

	@Override
	public void handle(MouseEvent event) {
		gridWindow.setX(event.getSceneX());
		gridWindow.setY(event.getSceneY());	
		synchronized (gridWindow.getMouseLock()) {
			gridWindow.notifyAllMouseClick();
		}
	}
}

public class GridWindow {
	private int sizeAbs, sizeOrd, sideLength, lengthAbs, lengthOrd;
	
	private Stage primaryStage;
	private BorderPane root;
	private Scene scene;
	private Canvas canvas;
	private GraphicsContext gc;
	
	private int x; // abs of the square where the mouse was clicked
	private int y; // ord of the sauqre where the mouse was clicked 
	
	private Object mouseLock;
	private Object threadLock;
	
	public GridWindow(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Reversi");
			
		mouseLock = new Object();
		threadLock = new Object();
	}
	
	public GridWindow(Stage primaryStage, int sizeAbs, int sizeOrd, int maxLength){
		this.primaryStage = primaryStage;
		this.sizeAbs = sizeAbs;
		this.sizeOrd = sizeOrd;
		sideLength = (sizeAbs >= sizeOrd) ? maxLength/sizeAbs : maxLength/sizeOrd;
		lengthAbs = sideLength*sizeAbs;
		lengthOrd = sideLength*sizeOrd;
		
		root = new BorderPane();
		root.setMinSize(lengthAbs, lengthOrd);
		root.setMaxSize(lengthAbs, lengthOrd);
		root.setPrefSize(lengthAbs, lengthOrd);
		primaryStage.setTitle("Green Square Adventure");
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
			
		Canvas canvas = new Canvas(lengthAbs, lengthOrd);
		root.getChildren().add(canvas);
		
		canvas.setOnMouseClicked(new MouseClick(this));
		gc = canvas.getGraphicsContext2D();
		
		drawGrid();
		
		primaryStage.show();
		
		mouseLock = new Object();
	}
	
	// Methods for drawing
	public void drawGrid() {
		for (int i = 0; i <= sizeAbs; ++i)
			drawLine(i*sideLength, 0, i*sideLength, lengthOrd);
		for (int i = 0; i <= sizeOrd; ++i)
			drawLine(0, i*sideLength, lengthAbs, i*sideLength);
	}
	
	public void setGrid(int sizeAbs, int sizeOrd, int maxLength) {
		this.sizeAbs = sizeAbs;
		this.sizeOrd = sizeOrd;
		sideLength = (sizeAbs >= sizeOrd) ? maxLength/sizeAbs : maxLength/sizeOrd;
		lengthAbs = sideLength*sizeAbs;
		lengthOrd = sideLength*sizeOrd;
		
		root = new BorderPane();
		root.setMinSize(lengthAbs, lengthOrd);
		root.setMaxSize(lengthAbs, lengthOrd);
		root.setPrefSize(lengthAbs, lengthOrd);
		scene = new Scene(root, lengthAbs, lengthOrd);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
			
		canvas = new Canvas(lengthAbs, lengthOrd);
		root.getChildren().add(canvas);
		
		canvas.setOnMouseClicked(new MouseClick(this));
		gc = canvas.getGraphicsContext2D();
		
		drawGrid();
		
		primaryStage.show();
	}
	
	public void drawLine(double x1, double y1, double x2, double y2) {
		gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
		gc.strokeLine(x1, y1, x2, y2);
	}
	
	public void drawSquare(int x, int y, Paint color) {
		gc.setFill(color);
		gc.fillRect(x*sideLength, y*sideLength, sideLength, sideLength);
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(2);
		gc.strokeRect(x*sideLength, y*sideLength, sideLength, sideLength);
	}
	
	public void write(String str, Paint fillColor, Paint strokeColor, double x, double y, double size) {
		gc.setFill(fillColor);
		gc.setStroke(strokeColor);
		gc.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
		gc.setFont(Font.font("Verdana", size));
		gc.strokeText(str, x, y, lengthAbs);
		gc.fillText(str, x, y, lengthAbs);
	}
	
	public void writeCenterCrimsonGold(String str, double size) {
		write(str, Color.CRIMSON, Color.GOLD, lengthAbs/2, lengthOrd/2, size);
	}
	
	// Methods to get information from the mouse
	public int getX() { return x; }
	public void setX(double x) { this.x = (int) x / sideLength; }
	public int getY() { return y; }
	public void setY(double y) { this.y = (int) y / sideLength; }
	
	// Methods to synchronize threads on mouse click
	public Object getMouseLock() { return mouseLock; }
	public Object getThreadLock() { return threadLock; }
	 
	public void waitForMouseClick() { 
		try {
			mouseLock.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void notifyAllMouseClick() {
		synchronized (mouseLock) {
			mouseLock.notify();
		}
	}
	
	public synchronized void notifyAllThreadLock() {
		synchronized (threadLock) {
			threadLock.notify();
		}
	}
	
	public void show() {
		primaryStage.show();
	}
}
