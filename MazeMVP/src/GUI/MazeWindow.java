package GUI;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import presenter.Properties;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;

public class MazeWindow extends BasicWindow {

	private MazeDisplay mazeDisplay;
	private Properties properties;
	
	
	public MazeWindow(Properties p) {
		this.properties = p;
	}
	
	@Override
	@SuppressWarnings("unused")
	protected void initWidgets() {
	
		
		GridLayout gridLayout = new GridLayout(2, false);
		shell.setLayout(gridLayout);				
		shell.setText("Maze3d Game");
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		shell.setBackgroundImage(new Image(null, "resources/images/light-blue-background.jpg"));
		
		
		//Initialize the menu bar
		MazeMenu menu = new MazeMenu(this);
		
		// Open in center of screen
		Rectangle bounds = display.getPrimaryMonitor().getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);

		// handle with the RED X
		shell.addListener(SWT.Close, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				exit();
			}
		});
		
		Composite btnGroup = new Composite(shell, SWT.BORDER);
		RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
		btnGroup.setLayout(rowLayout);
		
		Button btnGenerateMaze = new Button(btnGroup, SWT.PUSH);
		btnGenerateMaze.setText("Generate maze");	
		
		btnGenerateMaze.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				showGenerateMazeOptions();
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		Button btnSolveMaze = new Button(btnGroup, SWT.PUSH);
		btnSolveMaze.setText("Solve maze");
		
		
		Button btnDisplayMaze = new Button(btnGroup, SWT.PUSH);
		btnDisplayMaze.setText("Display maze");	
		
		btnDisplayMaze.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				showMaze("Maze1");
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		//Set layout to maze display
		mazeDisplay = new MazeDisplay(shell, SWT.NONE,this,null);
		mazeDisplay.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		mazeDisplay.setFocus();
	}

	protected void showGenerateMazeOptions() {
		
		
		Shell generateWindowShell = new Shell(display, SWT.TITLE | SWT.CLOSE);
		generateWindowShell.setText("Generate maze window");
		generateWindowShell.setLayout(new GridLayout(2, false));
		generateWindowShell.setSize(215, 215);
		generateWindowShell.setBackgroundImage(new Image(null, "resources/images/backgroundSmall.png"));
		generateWindowShell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		
		// Open in center of screen
		Rectangle bounds = display.getPrimaryMonitor().getBounds();
		Rectangle rect = generateWindowShell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		generateWindowShell.setLocation(x, y);
		
		Label lblHead = new Label(generateWindowShell, SWT.BOLD);
		FontData fontData = lblHead.getFont().getFontData()[0];
		Font font = new Font(display, new FontData(fontData.getName(), fontData.getHeight()+1, SWT.BOLD));
		lblHead.setFont(font);
		lblHead.setText("Enter maze dimensions");
		lblHead.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));
		
		Label lblFloors = new Label(generateWindowShell, SWT.NONE);
		lblFloors.setText("Floors: ");
		Text txtFloors = new Text(generateWindowShell, SWT.BORDER);
		txtFloors.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		txtFloors.setText("3");
		
		Label lblRows = new Label(generateWindowShell, SWT.NONE);
		lblRows.setText("Rows: ");
		Text txtRows = new Text(generateWindowShell, SWT.BORDER);
		txtRows.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		txtRows.setText("6");
		
		Label lblCols = new Label(generateWindowShell, SWT.NONE);
		lblCols.setText("Columns: ");
		Text txtCols = new Text(generateWindowShell, SWT.BORDER);
		txtCols.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		txtCols.setText("6");
		
		Label lblName = new Label(generateWindowShell, SWT.NONE);
		lblName.setText("Name: ");
		Text txtName = new Text(generateWindowShell, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		Random rand = new Random();
		int  n = rand.nextInt(100) + 1;
		String randomMazeName = "Maze"+Integer.toString(n);
		txtName.setText(randomMazeName);
		
		Button btnStartGame = new Button(generateWindowShell, SWT.PUSH);
		btnStartGame.setText("Play!");
		btnStartGame.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		btnStartGame.addSelectionListener(new SelectionListener() {

			/**
			 * take all the floors, cols, rows and make them from Text Box 
			 * 
			 * @param SelectionEvent
			 */
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setChanged();
				notifyObservers("generate_3d_maze" + " " + txtName.getText()
						+ " " + txtFloors.getText() + " " + txtRows.getText()
						+ " " + txtCols.getText() + " "
						+ properties.getAlgorithmToGenerateMaze());
				//Close the dialog window
				generateWindowShell.dispose();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) { }
			
		});

		generateWindowShell.open();

		mazeDisplay.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				String direction = null;
				switch (e.keyCode) {
				case SWT.ARROW_RIGHT:
					direction = "right";
					break;
				case SWT.ARROW_LEFT:
					direction = "left";
					break;
				case SWT.ARROW_UP:
					direction = "up";
					break;
				case SWT.ARROW_DOWN:
					direction = "down";
					break;
				case SWT.PAGE_DOWN:
					direction = "forward";
					break;
				case SWT.PAGE_UP:
					direction = "backward";
					break;
				default:
					break;
				}
				if (direction != null) {
					showMessageBox(direction);
					try {
						mazeDisplay.moveChracter(direction);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					//redraw();
				}
			}
		});
	}

	@Override
	public void notifyMazeIsReady(String name) {

		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				setChanged();
				notifyObservers("display " + name);
			}
		});
	
	
	}

	public void showMessageBox(String str) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				MessageBox msg = new MessageBox(shell, SWT.ICON_INFORMATION);
				msg.setMessage(str);
				msg.open();
			}
		});
	}
	

	@Override
	public void start() {
		run();		
	}

	@Override
	public void showDirPath(String dirArray) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showError(String message) {
		showMessageBox(message);
		
	}


	@Override
	public void showDisplayCrossSectionBy(String crossMazeBySection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showSaveMaze(String str) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showLoadMaze(String str) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showSolve(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showDisplaySolution(String sol) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printMenu(String menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showMaze(String mazeByteArrString) {
		try {

			byte[] byteArr = mazeByteArrString.getBytes(StandardCharsets.UTF_8);
			// Convert maze from byeArray
			Maze3d maze3d = new Maze3d(byteArr);
			Position startPos = maze3d.getFirstCellAfterShellPostition(maze3d.getStartPosition());
			mazeDisplay.setCharacterPosition(startPos);
			int[][] crossSection = maze3d.getCrossSectionByX((startPos.getX()-1)/2);
			mazeDisplay.setCrossSection(crossSection, null, null);
			mazeDisplay.setGoalPosition(maze3d.getGoalPosition());
			mazeDisplay.setMazeName(maze3d.getName());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
