
package fr.isen.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

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
public class Board extends JPanel implements ActionListener {

  /**
   * Project : Tetris <br/>
   * Package : fr.isen.tetris <br/>
   * Nom : TAdapter <br/>
   * 
   * Description : Adapter pour les touches du clavier <br/>
   */
  class TAdapter extends KeyAdapter {

    /**
     * {@inheritDoc}
     * 
     * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
     */
    @Override
    public void keyPressed(final KeyEvent e) {

      if (!Board.this.isStarted || Board.this.curPiece.getShape() == Tetrominoes.NoShape) {
        return;
      }

      final int keycode = e.getKeyCode();

      // Gestion de la pause
      if (keycode == 'p' || keycode == 'P') {
        Board.this.pause();
        return;
      }

      if (Board.this.isPaused) {
        return;
      }

      // Gestion des mouvements
      switch (keycode) {
        case KeyEvent.VK_LEFT:
          Board.this.tryMove(Board.this.curPiece, Board.this.curX - 1, Board.this.curY);
          break;
        case KeyEvent.VK_RIGHT:
          Board.this.tryMove(Board.this.curPiece, Board.this.curX + 1, Board.this.curY);
          break;
        case KeyEvent.VK_DOWN:
          Board.this.tryMove(Board.this.curPiece.rotateRight(), Board.this.curX, Board.this.curY);
          break;
        case KeyEvent.VK_UP:
          Board.this.tryMove(Board.this.curPiece.rotateLeft(), Board.this.curX, Board.this.curY);
          break;
        case KeyEvent.VK_SPACE:
          Board.this.dropDown();
          break;
        case 'd':
          Board.this.oneLineDown();
          break;
        case 'D':
          Board.this.oneLineDown();
          break;
      }
    }
  }

  /**
   * Serial UID
   */
  private static final long serialVersionUID = -6711684368736388612L;

  /**
   * Timer
   */
  private final Timer timer;

  /**
   * Indicateur permettant de savoir si une pièce à atteind un point de blocage (le fond ou une autre pièce) l'empechant de descendre
   */
  private boolean isFallingFinished = false;

  /**
   * Flag de jeu commencé
   */
  private boolean isStarted = false;

  /**
   * Flag de jeu en pause
   */
  private boolean isPaused = false;

  /**
   * Score
   */
  private int numLinesRemoved = 0;

  /**
   * Position courrante sur l'axe des abscisses
   */
  private int curX = 0;

  /**
   * Position courrante sur l'axe des ordonnées
   */
  private int curY = 0;

  /**
   * Barre de statut
   */
  private final JLabel statusbar;

  /**
   * Pièce courrament en jeu
   */
  private Shape curPiece;

  /**
   * Tableau de jeu avec toutes les pièces
   */
  private final Tetrominoes[] board;

  /**
   * Constructeur
   * 
   * @param parent
   *          fenêtre de jeu
   */
  public Board(final Tetris parent) {

    this.setFocusable(true);
    this.curPiece = new Shape();
    this.timer = new Timer(400, this);
    this.timer.start();

    this.statusbar = parent.getStatusBar();
    this.board = new Tetrominoes[Constants.BOARD_WIDTH * Constants.BOARD_HEIGHT];
    this.addKeyListener(new TAdapter());
    this.clearBoard();
  }

  /**
   * {@inheritDoc}
   * 
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  @Override
  public void actionPerformed(final ActionEvent e) {
    if (this.isFallingFinished) {
      this.isFallingFinished = false;
      this.newPiece();
    } else {
      this.oneLineDown();
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.JComponent#paint(java.awt.Graphics)
   */
  @Override
  public void paint(final Graphics g) {
    super.paint(g);

    final Dimension size = this.getSize();
    final int boardTop = (int) size.getHeight() - Constants.BOARD_HEIGHT * this.squareHeight();

    for (int i = 0; i < Constants.BOARD_HEIGHT; ++i) {
      for (int j = 0; j < Constants.BOARD_WIDTH; ++j) {
        final Tetrominoes shape = this.shapeAt(j, Constants.BOARD_HEIGHT - i - 1);
        if (shape != Tetrominoes.NoShape) {
          this.drawSquare(g, 0 + j * this.squareWidth(), boardTop + i * this.squareHeight(), shape);
        }
      }
    }

    if (this.curPiece.getShape() != Tetrominoes.NoShape) {
      for (int i = 0; i < 4; ++i) {
        final int x = this.curX + this.curPiece.x(i);
        final int y = this.curY - this.curPiece.y(i);
        this.drawSquare(g, 0 + x * this.squareWidth(), boardTop + (Constants.BOARD_HEIGHT - y - 1) * this.squareHeight(), this.curPiece.getShape());
      }
    }
  }

  /**
   * Démarre le jeu
   */
  public void start() {
    if (this.isPaused) {
      return;
    }

    this.isStarted = true;
    this.isFallingFinished = false;
    this.numLinesRemoved = 0;
    this.clearBoard();

    this.newPiece();
    this.timer.start();
  }

  /**
   * Reset
   */
  private void clearBoard() {
    for (int i = 0; i < Constants.BOARD_HEIGHT * Constants.BOARD_WIDTH; ++i) {
      this.board[i] = Tetrominoes.NoShape;
    }
  }

  /**
   * Dessine un bloc
   * 
   * @param g
   *          le graphique à utiliser
   * @param x
   *          l'abscisse
   * @param y
   *          l'ordonné
   * @param shape
   *          la forme à dessiner
   */
  private void drawSquare(final Graphics g, final int x, final int y, final Tetrominoes shape) {
    final Color colors[] = { new Color(0, 0, 0), new Color(204, 102, 102), new Color(102, 204, 102), new Color(102, 102, 204), new Color(204, 204, 102), new Color(204, 102, 204),
        new Color(102, 204, 204), new Color(218, 170, 0), new Color(78, 100, 0), new Color(78, 255, 0) };

    final Color color = colors[shape.ordinal()];

    g.setColor(color);
    g.fillRect(x + 1, y + 1, this.squareWidth() - 2, this.squareHeight() - 2);

    g.setColor(color.brighter());
    g.drawLine(x, y + this.squareHeight() - 1, x, y);
    g.drawLine(x, y, x + this.squareWidth() - 1, y);

    g.setColor(color.darker());
    g.drawLine(x + 1, y + this.squareHeight() - 1, x + this.squareWidth() - 1, y + this.squareHeight() - 1);
    g.drawLine(x + this.squareWidth() - 1, y + this.squareHeight() - 1, x + this.squareWidth() - 1, y + 1);
  }

  /**
   * Fait descendre la pièce le plus bas possible
   */
  private void dropDown() {
    int newY = this.curY;
    while (newY > 0) {
      if (!this.tryMove(this.curPiece, this.curX, newY - 1)) {
        break;
      }
      --newY;
    }
    this.pieceDropped();
  }

  /**
   * Créé une nouvelle pièce
   */
  private void newPiece() {
    this.curPiece.setRandomShape();
    this.curX = Constants.BOARD_WIDTH / 2 + 1;
    this.curY = Constants.BOARD_HEIGHT - 1 + this.curPiece.minY();

    if (!this.tryMove(this.curPiece, this.curX, this.curY)) {
      this.curPiece.setShape(Tetrominoes.NoShape);
      this.timer.stop();
      this.isStarted = false;
      this.statusbar.setText("game over");
    }
  }

  /**
   * Fait descendre la pièce d'une ligne
   */
  private void oneLineDown() {
    if (!this.tryMove(this.curPiece, this.curX, this.curY - 1)) {
      this.pieceDropped();
    }
  }

  /**
   * Met e jeu en pause
   */
  private void pause() {
    if (!this.isStarted) {
      return;
    }

    this.isPaused = !this.isPaused;
    if (this.isPaused) {
      this.timer.stop();
      this.statusbar.setText("paused");
    } else {
      this.timer.start();
      this.statusbar.setText(String.valueOf(this.numLinesRemoved));
    }
    this.repaint();
  }

  /**
   * Action effectuée lorsque la pièce courrante est bloquée
   */
  private void pieceDropped() {
    for (int i = 0; i < 4; ++i) {
      final int x = this.curX + this.curPiece.x(i);
      final int y = this.curY - this.curPiece.y(i);
      this.board[y * Constants.BOARD_WIDTH + x] = this.curPiece.getShape();
    }

    this.removeFullLines();

    if (!this.isFallingFinished) {
      this.newPiece();
    }
  }

  /**
   * Supprime une ligne de blocs
   */
  private void removeFullLines() {
    int numFullLines = 0;

    for (int i = Constants.BOARD_HEIGHT - 1; i >= 0; --i) {
      boolean lineIsFull = true;

      for (int j = 0; j < Constants.BOARD_WIDTH; ++j) {
        if (this.shapeAt(j, i) == Tetrominoes.NoShape) {
          lineIsFull = false;
          break;
        }
      }

      if (lineIsFull) {
        ++numFullLines;
        for (int k = i; k < Constants.BOARD_HEIGHT - 1; ++k) {
          for (int j = 0; j < Constants.BOARD_WIDTH; ++j) {
            this.board[k * Constants.BOARD_WIDTH + j] = this.shapeAt(j, k + 1);
          }
        }
      }
    }

    if (numFullLines > 0) {
      this.numLinesRemoved += numFullLines;
      this.statusbar.setText(String.valueOf(this.numLinesRemoved));
      this.isFallingFinished = true;
      this.curPiece.setShape(Tetrominoes.NoShape);
      this.repaint();
    }
  }

  /**
   * Vérifie la forme pour une position donnée
   * 
   * @param x
   *          abscisse à vérifier
   * @param y
   *          ordonné à vérifier
   * @return la pièce trouvée
   */
  private Tetrominoes shapeAt(final int x, final int y) {
    return this.board[y * Constants.BOARD_WIDTH + x];
  }

  /**
   * @return la hauteur du plateau de jeu
   */
  private int squareHeight() {
    return (int) this.getSize().getHeight() / Constants.BOARD_HEIGHT;
  }

  /**
   * @return la largeur du plateau de jeu
   */
  private int squareWidth() {
    return (int) this.getSize().getWidth() / Constants.BOARD_WIDTH;
  }

  /**
   * Essaie de deplacer une pièce et le fait si c'est possible
   * 
   * @param newPiece
   *          la pièce à déplacer
   * @param newX
   *          nouvelle position sur l'absisse
   * @param newY
   *          nouvelle position sur l'ordonnée
   * @return false si la pièce ne peux pas bouger, true si elle a été déplacé
   */
  private boolean tryMove(final Shape newPiece, final int newX, final int newY) {
    for (int i = 0; i < 4; ++i) {
      final int x = newX + newPiece.x(i);
      final int y = newY - newPiece.y(i);
      if (x < 0 || x >= Constants.BOARD_WIDTH || y < 0 || y >= Constants.BOARD_HEIGHT) {
        return false;
      }
      if (this.shapeAt(x, y) != Tetrominoes.NoShape) {
        return false;
      }
    }

    this.curPiece = newPiece;
    this.curX = newX;
    this.curY = newY;
    this.repaint();
    return true;
  }
}
