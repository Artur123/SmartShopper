package at.jku.smartshopper.backend;

import at.jku.smartshopper.objects.Article;

/**
 * 
 *Interface to get the ArticleData from the Server 
 */
public interface IArticleService {
	
	public Article getArticleById(String barcode);

}
