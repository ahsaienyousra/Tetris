/**
 * 
 */
package fr.isen.tetris;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.commons.lang3.StringUtils;

/**
 * Project : Tetris <br/>
 * Package : fr.isen.tetris <br/>
 * Nom : Tetris <br/>
 * 
 * Description : Classe représentant une forme géométrique <br/>
 * Possède une liste finie définie dans l'énumération imbriquée Tetrominoes
 * 
 * @author zetcode.com
 * @see http://zetcode.com/tutorials/javagamestutorial/tetris/
 */
public class Tetris extends JFrame {

  /**
   * Serial UID
   */
  private static final long serialVersionUID = 5926082683168881403L;

  /**
   * Barre de statut
   */
  private final JLabel statusbar;

  /**
   * Cnstructeur
   */
  public Tetris() {

    this.statusbar = new JLabel(" 0");
    this.add(this.statusbar, BorderLayout.SOUTH);
    final Board board = new Board(this);
    this.add(board);
    board.start();

    this.setSize(200, 400);
    this.setTitle(StringUtils.leftPad("Tetris", 10, "*"));
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  /**
   * Main - Lanceur pour le programme
   * 
   * @param args
   *          les arguments potentiels (non attendus)
   */
  public static void main(final String[] args) {

    final Tetris game = new Tetris();
    game.setLocationRelativeTo(null);
    game.setVisible(true);

  }

  /**
   * @return the statusBar
   */
  public JLabel getStatusBar() {
    return this.statusbar;
  }
}
