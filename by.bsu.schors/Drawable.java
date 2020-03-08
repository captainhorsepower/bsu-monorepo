package by.bsu.shchors;


/**
 * @author shchors_vs
 * @version 1.0
 * @created 02-Mar-2020 3:52:17 PM
 */
public interface Drawable {

	public void draw();

	public Point location();

	/**
	 * 
	 * @param newLocation    points
	 */
	public void move(Point newLocation);

}