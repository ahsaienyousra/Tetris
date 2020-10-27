package fr.isen.tetris;

import java.util.Random;

/**
 * Project : Tetris <br/>
 * Package : fr.isen.tetris <br/>
 * Nom : Shape <br/>
 * 
 * Description : Classe représentant une forme géométrique <br/>
 * Possède une liste finie définie dans l'énumération imbriquée Tetrominoes
 * 
 * @author zetcode.com
 * @see http://zetcode.com/tutorials/javagamestutorial/tetris/
 */
public class Shape {

  /**
   * Forme de la pièce
   */
  private Tetrominoes pieceShape;

  /**
   * Coordonnées de la pièce
   */
  private final int coords[][];

  /**
   * Table de coordonnées de la pièce
   */
  private int[][][] coordsTable;

  /**
   * Constructeur
   */
  public Shape() {
    this.coords = new int[4][2];
    this.setShape(Tetrominoes.NoShape);
  }

  /**
   * @return the shape
   */
  public Tetrominoes getShape() {
    return this.pieceShape;
  }

  /**
   * @return la position la plus à gauche de cette pièce
   */
  public int minX() {
    int m = this.coords[0][0];
    for (int i = 0; i < 4; i++) {
      m = Math.min(m, this.coords[i][0]);
    }
    return m;
  }

  /**
   * @return la position la plus basse de cette pièce
   */
  public int minY() {
    int m = this.coords[0][1];
    for (int i = 0; i < 4; i++) {
      m = Math.min(m, this.coords[i][1]);
    }
    return m;
  }

  /**
   * Effectue une rotation de la pièce sur la gauche
   * 
   * @return la pièce avec ces nouvelles coordonnées
   */
  public Shape rotateLeft() {
    if (this.pieceShape == Tetrominoes.SquareShape) {
      return this;
    }

    final Shape result = new Shape();
    result.pieceShape = this.pieceShape;

    for (int i = 0; i < 4; ++i) {
      result.setX(i, this.y(i));
      result.setY(i, -this.x(i));
    }
    return result;
  }

  /**
   * Effectue une rotation de la pièce sur la droite
   * 
   * @return la pièce avec ces nouvelles coordonnées
   */
  public Shape rotateRight() {
    if (this.pieceShape == Tetrominoes.SquareShape) {
      return this;
    }

    final Shape result = new Shape();
    result.pieceShape = this.pieceShape;

    for (int i = 0; i < 4; ++i) {
      result.setX(i, -this.y(i));
      result.setY(i, this.x(i));
    }
    return result;
  }

  /**
   * Permet de créer une pièce aléatoirement
   */
  public void setRandomShape() {
    final Random r = new Random();
    final int x = Math.abs(r.nextInt()) % 8 + 1;
    final Tetrominoes[] values = Tetrominoes.values();
    this.setShape(values[x]);
  }

  /**
   * Positionne une pièce précise
   * 
   * @param shape
   *          la pièce à créer
   */
  public void setShape(final Tetrominoes shape) {

    this.coordsTable = new int[][][] { { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } }, { { 0, -1 }, { 0, 0 }, { -1, 0 }, { -1, 1 } }, { { 0, -1 }, { 0, 0 }, { 1, 0 }, { 1, 1 } },
        { { 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 } }, { { -1, 0 }, { 0, 0 }, { 1, 0 }, { 0, 1 } }, { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } }, { { -1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } },
        { { 1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 }} , {{ 1, -1 }, { 0, -1 }, { 1, 1 }, { 0, 1 } } };

    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 2; ++j) {
        this.coords[i][j] = this.coordsTable[shape.ordinal()][i][j];
      }
    }
    this.pieceShape = shape;

  }

  /**
   * Donne la position horizontale d'un bloc de cette pièce pour un index donné
   * 
   * @param index
   *          l'index du bloc que l'on cherche
   * @return la position horizontale de ce bloc
   */
  public int x(final int index) {
    return this.coords[index][0];
  }

  /**
   * Donne la position verticale d'un bloc de cette pièce pour un index donné
   * 
   * @param index
   *          l'index du bloc que l'on cherche
   * @return la position verticale de ce bloc
   */
  public int y(final int index) {
    return this.coords[index][1];
  }

  /**
   * Positionne un bloc de cette pièce à un point précis sur l'axe des abscisse
   * 
   * @param index
   *          index du bloc à déplacer
   * @param x
   *          nouvel index sur l'axe des abscisse
   */
  private void setX(final int index, final int x) {
    this.coords[index][0] = x;
  }

  /**
   * Positionne un bloc de cette pièce à un point précis sur l'axe des ordonnées
   * 
   * @param index
   *          index du bloc à déplacer
   * @param x
   *          nouvel index sur l'axe des ordonnées
   */
  private void setY(final int index, final int y) {
    this.coords[index][1] = y;
  }
}
