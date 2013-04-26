package at.jku.smartshopper.backend;

import at.jku.smartshopper.objects.Basket;
import at.jku.smartshopper.objects.BasketList;


public interface IBasketService {
	
	public void putBasket(Basket basket, String username);
	public BasketList getAllBaskets(String username);
	public Basket getLatestBasket(Basket basket, String username);
	public Basket getBasketById(long timeStamp, String username);

}
