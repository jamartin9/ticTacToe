package ticTacToe;


public class TTTController  {

	private TTTModel model;
	private TTTView view;
	private final boolean SMART = true;

	public TTTController(int dim){
		model = new TTTModel(dim);
		view = new TTTView(this, model);
	}

	public TTTController(){
		this(3);
	}

	public void squareSelected(int i, int j) {

		if (model.gameOver() && model.score() == 0){
			endGame();
			return;
		}
		if (model.getSquare(i, j) != '.')
			return;
		model.playerSelectSquare(i, j);
		view.update();

		if (model.gameOver()){
			endGame();
			return;
		}

		model.playBestMove(SMART);
		view.update();

		if (model.gameOver()){
			endGame();
			return;
		}

	}

	public void play(){
		playFirst();
		view.start();
	}

	private void playFirst() {
		if (!view.message("Play first?")){
			model.computerPlayFirst();
		} else {
			model.resetSquares();
		}
		view.update();
	}



	private void endGame(){
		String message = "";

		switch(model.score()){
		case 0:
			message = "It's a tie!";
			break;
		case 1:
			message = "You lose!";
			break;
		case -1:
			message = "You win!";
		}

		String str = message + "\nWould you like to play again?";
		if (view.message(str)){
			model.resetSquares();
			playFirst();
		} else {
			view.dispose();
			System.exit(0);
		}
		view.update();
	}

	public static void main(String[] args) {
		new TTTController().play();
	}

}
