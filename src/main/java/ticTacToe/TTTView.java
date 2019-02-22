package ticTacToe;

import java.io.InputStream;
import java.util.HashMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class TTTView {

	private TTTController controller;
	private TTTModel model;
	private int dim;

	private Display display;
	private Shell shell;

	private final int WIDTH = 400;
	private final int HEIGHT = 360;

	private HashMap<String, Image> imageMap = new HashMap<String, Image>();

	private Canvas[][] squares;

	public TTTView(TTTController controller, TTTModel model) {
		dim = model.getDimension();
		this.model = model;
		this.controller = controller;

		display = new Display();
		shell = new Shell(display);
		shell.setSize(new Point(WIDTH, HEIGHT));

		shell.setLayout(new GridLayout(dim, true));
		shell.setBackground(display.getSystemColor(SWT.COLOR_BLACK));

		loadImages(display, imageMap);
		createSquares();
		centralize();
	}

	private void createSquares() {
		squares = new Canvas[dim][dim];
		Cursor hand = new Cursor(display, SWT.CURSOR_HAND);

		MouseListener listener = new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseUp(MouseEvent e) {
				Canvas canvas = (Canvas) e.widget;
				int[] indices = (int[]) canvas.getData();
				controller.squareSelected(indices[0], indices[1]);
			}

		};

		PaintListener onPaint = new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				Canvas canvas = (Canvas) e.widget;
				int[] indices = (int[]) canvas.getData();
				drawSquare(e.gc, indices[0], indices[1]);
			}

		};

		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				squares[i][j] = new Canvas(shell, SWT.NO_REDRAW_RESIZE);

				GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
				squares[i][j].setLayoutData(data);
				squares[i][j].setData(new int[] { i, j });
				squares[i][j].setCursor(hand);
				squares[i][j].addMouseListener(listener);
				squares[i][j].addPaintListener(onPaint);
			}
		}
	}

	private void drawSquare(GC gc, int i, int j) {
		Rectangle obnds = gc.getClipping();
		Image image = null;

		switch (model.getSquare(i, j)) {
		case 'X':
			image = imageMap.get("cross");
			break;
		case 'O':
			image = imageMap.get("naught");
			break;
		case '.':
			image = imageMap.get("clear");

		}
		Rectangle ibnds = image.getBounds();
		gc.drawImage(image, ibnds.x, ibnds.y, ibnds.width, ibnds.height,
				obnds.x, obnds.y, obnds.width, obnds.height);
	}

	public void update() {
		int dim = squares.length;
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				squares[i][j].redraw();
			}
		}
	}

	private void centralize() {
		Rectangle scrn = display.getClientArea();
		shell.setLocation((scrn.width - WIDTH) / 2, (scrn.height - HEIGHT) / 2);
	}

	public boolean message(String str) {
		MessageBox msg = new MessageBox(new Shell(display), SWT.ICON_QUESTION
				| SWT.YES | SWT.NO);
		msg.setText("Information");
		msg.setMessage(str);
		return msg.open() == SWT.YES;
	}

	private void loadImages(Display display, HashMap<String, Image> map) {

		InputStream streamNaught = TTTView.class
				.getResourceAsStream("/naught.png");
		InputStream streamCross = TTTView.class
				.getResourceAsStream("/cross.png");
		InputStream streamClear = TTTView.class
				.getResourceAsStream("/clear.png");

		map.put("naught", new Image(display, streamNaught));
		map.put("cross", new Image(display, streamCross));
		map.put("clear", new Image(display, streamClear));

	}

	public void start() {
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

}
