package at.jku.smartshopper.backend;

import at.jku.smartshopper.objects.Shop;

/**
 * Interface to get the Shop data form the SErver
 *
 */
public interface IShopService {
	
	public Shop getShop(Long shopId);

}
