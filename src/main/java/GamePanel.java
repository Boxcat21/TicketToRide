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
	private ArrayList<TrainCard> chosenTrainCards;
	private ArrayList<Player> playerTracks;
	private ArrayList<Integer> selectionIndecies;
	private int minChoose;
	private int selectSize;
	private boolean setup;
	private int count;
	public GamePanel() throws IOException {
		setup = true;
		count = 0;
		setSize(1920, 1080);
		setVisible(true);
		addMouseListener(this);

		game = new GameState();
		clickedEdgeIndecies = new ArrayList<Integer>();
		chosenTrainCards = new ArrayList<>();
		playerTracks = new ArrayList<>();
		selectionIndecies = new ArrayList<>();

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
		if(setup) {
			chooseCards(e);
			placeEdge(e);
			if(!(chosenTrainCards.size() > 0)) {	
				arrowClick(e);
				obtainCards(e);
			}
			repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void paintComponent(Graphics g) {
		if (!game.isEnded()) {
			BoardDrawer.drawBoard(g, clickedEdgeIndecies, playerTracks);
			HandDrawer.drawHand(g, game.curPlayer(), chosenTrainCards);
			//if (game.getDisplayContracts() != null || game.getDisplayContracts().size() > 0)
			//	HandDrawer.drawContractCards(g, game.getDisplayContracts());
			HandDrawer.drawContractCards(g, game.curPlayer().getContracts());

			if (game.isChoosingContracts())
				HandDrawer.drawContractSelection(g, game.getDisplayContracts(), selectionIndecies, new Color(184, 134, 11));
			g.setColor(new Color(184, 134, 11));
			g.fillRect(1535, 0, 1920 - 1535, 1080);
			g.setColor(Color.BLACK);
			g.drawRect(1535, 0, 1920 - 1535, 1080);

			DataDrawer.drawDisplayCards(g, game.getDisplayCards());
			ArrayList<Player> temp = new ArrayList<>();
			temp.addAll(game.getPlayerList());
			temp.add(game.curPlayer());
			DataDrawer.drawData(g, temp);
		}
		/*
		 * BoardDrawer.drawBoard(g, this.clickedEdgeIndecies); HandDrawer.drawHand(g,
		 * p); HandDrawer.drawContractSelection(g, game.getDisplayContracts() new
		 * ArrayList<ContractCard>()); HandDrawer.drawContractCards(g,
		 * p.getContracts());
		 * 
		 * g.setColor(new Color(184, 134,11)); g.fillRect(1535, 0, 1920-1535, 1080);
		 * g.setColor(Color.BLACK); g.drawRect(1535, 0, 1920-1535, 1080);
		 * 
		 * HandDrawer.drawPlayer(g, game.curPlayer()); DataDrawer.drawDisplayCards(g,
		 * p.getTrainCards()); DataDrawer.drawCurPlayer(g, p);
		 */
	}
	private void chooseCards(MouseEvent e) {
		for (int i = 0; i < HandDrawer.clickableAdd.size(); i++) {
			if (HandDrawer.clickableAdd.get(i).contains(e.getPoint())) {
				if(game.curPlayer().cardIndex(GameState.TRAIN_COLORS[i]) != -1) {
					TrainCard c = game.curPlayer().getTrainCards().remove(game.curPlayer().cardIndex(GameState.TRAIN_COLORS[i]));
					chosenTrainCards.add(c);
					break;
				}
			}
		}
		for (int i = 0; i < HandDrawer.clickableSub.size(); i++) {
			if (HandDrawer.clickableSub.get(i).contains(e.getPoint())) {
				for(int p = 0; p < chosenTrainCards.size(); p++) {
					if(chosenTrainCards.get(p).getColor().equals(GameState.TRAIN_COLORS[i])) {
						game.curPlayer().addTrainCard(chosenTrainCards.remove(p));
						break;
					}
				}
			}
		}
	}
	private void placeEdge(MouseEvent e) {
		int index = BoardDrawer.edgeClick(e, game);
		boolean check = false;
		Player prev = game.curPlayer();
		if (index != -1) {
			check = game.placeTrain(game.getEdges().get(index), chosenTrainCards);
		}
		if (check) {
			clickedEdgeIndecies.add(index);
			playerTracks.add(prev);
			chosenTrainCards = new ArrayList<TrainCard>();
		}
	}
	private void arrowClick(MouseEvent e) {
		if (HandDrawer.clickableArrow.get(0).contains(e.getPoint()))
			HandDrawer.advanceCard(game.curPlayer(), -1);
		if (HandDrawer.clickableArrow.get(1).contains(e.getPoint()))
			HandDrawer.advanceCard(game.curPlayer(), 1);
	}
	private void obtainCards(MouseEvent e) {
		if(DataDrawer.getTrainDeck().contains(e.getPoint())) {
			game.drawTrainCard();
		}
		for(int i = 0; i < DataDrawer.clickableDisplayCards.size(); i++) {
			Rectangle r = DataDrawer.clickableDisplayCards.get(i);
			if(r.contains(e.getPoint())) {
				game.chooseTrainCard(i);
			}
		}
		if(DataDrawer.getTicketDeck().contains(e.getPoint())) {
			game.drawContracts(3);
		}
	}
	private boolean contractClick(MouseEvent e) {
		for(int i = 0; i < HandDrawer.getClickableContracts().size(); i++) {
			Rectangle r = HandDrawer.getClickableContracts().get(i);
			if(r.contains(e.getPoint()) && i == HandDrawer.getClickableContracts().size()-1) {
				if(selectionIndecies.size()>minChoose) {
					game.chooseContractCard(selectionIndecies, selectSize);
					return true;
				}
			}
			else if(r.contains(e.getPoint())) {
				if(!(selectionIndecies.contains(i)))
					selectionIndecies.add(i);
				else
					selectionIndecies.remove(i);
			}
		}
		return false;
	}
}
