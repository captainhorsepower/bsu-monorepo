package by.bsu.shchors;


/**
 * @author shchors_vs
 * @version 1.0
 * @created 02-Mar-2020 3:52:17 PM
 */
public abstract class Figure implements Drawable {

	private Point center;
	private Color borderColor;
	public Point m_Point;



	public void finalize() throws Throwable {

	}

	public Figure(){

	}

	public Point getCenter(){
		return null;
	}

	public Color getBorderColor(){
		return null;
	}

	/**
	 * 
	 * @param borderColor
	 */
	public void setBorderColor(Color borderColor){

	}

	/**
	 * calls getCenter
	 */
	public Point location(){
		return null;
	}

	/**
	 * 
	 * @param newCenter    newLocation
	 */
	public void setCenter(Point newCenter){

	}

	protected void init(){

	}

	public void draw(){

	}

	/**
	 * 
	 * @param newLocation    points
	 */
	public void move(Point newLocation){

	}

}