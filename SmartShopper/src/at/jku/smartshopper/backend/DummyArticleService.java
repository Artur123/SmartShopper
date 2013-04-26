package at.jku.smartshopper.backend;

import at.jku.smartshopper.objects.Article;

public class DummyArticleService implements IArticleService{

	@Override
	public Article getArticleById(String barcode) {
		Article a = new Article();
		a.setBarcode("12345");
		a.setName("Mineralwasser");
		a.setPrice(15.29);
		
		return a;
	}

}
