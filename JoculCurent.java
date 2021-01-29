package wow;

import java.io.Serializable;

public class JoculCurent implements Serializable {
	public int highscore;
	public int score;
	public int highest;
	Tile[][] tiles;
	public JoculCurent(int highscore, int score, int highest, Tile[][] tiles) {
		super();
		this.highscore = highscore;
		this.score = score;
		this.highest = highest;
		this.tiles = tiles;
	}
	
	
}
