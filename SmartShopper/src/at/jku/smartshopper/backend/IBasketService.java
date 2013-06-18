package at.jku.smartshopper.backend;

import at.jku.smartshopper.objects.Basket;
import at.jku.smartshopper.objects.BasketList;

/**
 * Interface to contact Server to put and get Baskets
 *
 */
public interface IBasketService {
	
	public void putBasket(Basket basket);
	public BasketList getAllBaskets();
	public Basket getLatestBasket();
	public Basket getBasketById(long timeStamp);

}
