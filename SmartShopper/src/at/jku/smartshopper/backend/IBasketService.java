package at.jku.smartshopper.backend;

import at.jku.smartshopper.objects.Basket;
import at.jku.smartshopper.objects.BasketList;

/**
 * Interface to contact Server to put and get Baskets
 *
 */
public interface IBasketService {
	/**
	 * Saves a Basket on the Server
	 * @param basket
	 */
	public void putBasket(Basket basket);
	/**
	 * Gets all Baskets from the Server
	 * @return
	 */
	public BasketList getAllBaskets();
	/**
	 * 
	 * @return
	 */
	public Basket getLatestBasket();
	/**
	 * 
	 * @param timeStamp
	 * @return
	 */
	public Basket getBasketById(long timeStamp);

}
