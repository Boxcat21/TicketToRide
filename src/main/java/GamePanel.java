import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements MouseListener {

	private GameState game;
	private ArrayList<Integer> clickedEdgeIndecies;
	private ArrayList<String> chosenTrainCards;
	private ArrayList<Player> playerTracks;
	public GamePanel() throws IOException {
		setSize(1920, 1080);
		setVisible(true);
		addMouseListener(this);

		game = new GameState();
		clickedEdgeIndecies = new ArrayList<Integer>();
		chosenTrainCards= new ArrayList<>();
		playerTracks = new ArrayList<>();
		
		HandDrawer.init();
		DataDrawer.init();	
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(chosenTrainCards.size() > 0) {
			for (int i = 0; i < HandDrawer.clickableAdd.size(); i++) {
				if(HandDrawer.clickableAdd.get(i).contains(e.getPoint())) {
					ArrayList<TrainCard> cards = game.getCurPlayer().getTrainCards();
					
					for(int x = 0; x < cards.size(); x++) {
						
					
					chosenTrainCards.add(GameState.TRAIN_COLORS[i]);
				}	
			}
			for (int i = 0; i < HandDrawer.clickableSub.size(); i++) {
				if(HandDrawer.clickableSub.get(i).contains(e.getPoint())) {
					if(chosenTrainCards.contains(GameState.TRAIN_COLORS[i]))
						chosenTrainCards.remove(GameState.TRAIN_COLORS[i]);
				}	
			}
			int index = BoardDrawer.edgeClick(e, game);
			boolean check = false;
			Player prev = game.getCurPlayer();
			if(index != -1) {
				check = game.placeTrain(game.getEdges().get(index), chosenTrainCards);
			}
			if(check) {
				clickedEdgeIndecies.add(index);
				playerTracks.add(prev);
				chosenTrainCards = new ArrayList<String>();
			}
			else
				return;
		}
		
		
		for (int i = 0; i < HandDrawer.clickableAdd.size(); i++) {
			if(HandDrawer.clickableAdd.get(i).contains(e.getPoint())) {
				chosenTrainCards.add(GameState.TRAIN_COLORS[i]);
			}	
		}
		 
		for (int i = 0; i < HandDrawer.clickableSub.size(); i++) {
			if(HandDrawer.clickableSub.get(i).contains(e.getPoint())) {
				if(chosenTrainCards.contains(GameState.TRAIN_COLORS[i]))
					chosenTrainCards.remove(GameState.TRAIN_COLORS[i]);
			}	
		}

		if ( HandDrawer.clickableArrow.get(0).contains(e.getPoint()))
			HandDrawer.advanceCard(game.getCurPlayer(), -1);
		if ( HandDrawer.clickableArrow.get(1).contains(e.getPoint()))
			HandDrawer.advanceCard(game.getCurPlayer(), 1);
		
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void paintComponent(Graphics g) {
		if (!game.isEnded()) {
			BoardDrawer.drawBoard(g, clickedEdgeIndecies, playerTracks);
			HandDrawer.drawHand(g, game.getCurPlayer());
			if (game.getDisplayContracts() != null)
				HandDrawer.drawContractCards(g, game.getDisplayContracts());
			HandDrawer.drawContractCards(g, game.getCurPlayer().getContracts());

			if (game.isChoosingContracts())
				HandDrawer.drawContractSelection(g, game.getDisplayContracts());
		}
		g.setColor(new Color(184, 134, 11));
		g.fillRect(1535, 0, 1920 - 1535, 1080);
		g.setColor(Color.BLACK);
		g.drawRect(1535, 0, 1920 - 1535, 1080);

		DataDrawer.drawDisplayCards(g, game.getCurPlayer().getTrainCards());
		DataDrawer.drawCurPlayer(g, game.getCurPlayer());
		ArrayList<Player> temp = new ArrayList<>();
		temp.addAll(game.getPlayerList());
		temp.add(game.getCurPlayer());
		DataDrawer.drawData(g, temp);
		/*
		 * BoardDrawer.drawBoard(g, this.clickedEdgeIndecies); HandDrawer.drawHand(g,
		 * p); HandDrawer.drawContractSelection(g, game.getDisplayContracts() new
		 * ArrayList<ContractCard>()); HandDrawer.drawContractCards(g,
		 * p.getContracts());
		 * 
		 * g.setColor(new Color(184, 134,11)); g.fillRect(1535, 0, 1920-1535, 1080);
		 * g.setColor(Color.BLACK); g.drawRect(1535, 0, 1920-1535, 1080);
		 * 
		 * HandDrawer.drawPlayer(g, game.getCurPlayer()); DataDrawer.drawDisplayCards(g,
		 * p.getTrainCards()); DataDrawer.drawCurPlayer(g, p);
		 */
	}

}
