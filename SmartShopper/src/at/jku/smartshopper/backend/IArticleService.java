package at.jku.smartshopper.backend;

import at.jku.smartshopper.objects.Article;

public interface IArticleService {
	
	public Article getArticleById(String barcode);

}
