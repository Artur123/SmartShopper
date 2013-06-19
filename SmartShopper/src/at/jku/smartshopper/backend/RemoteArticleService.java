package at.jku.smartshopper.backend;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import at.jku.smartshopper.objects.Article;

public class RemoteArticleService implements IArticleService {

	@Override
	public Article getArticleById(String barcode) {
		
		try {
			DisableSSLCertificateCheckUtil.disableChecks();
			//create instance of request provider which helps with some convencience methods

			RequestProvider provider = new RequestProvider();
			HttpHeaders header = provider.getHttpHeader();

			RestTemplate restTemplate = provider.getRestTemplate();
			//create map which contains url data

			Map<String, Object> urlVariables = new HashMap<String, Object>();
			urlVariables.put("barcode", barcode);

			//make rest request

			ResponseEntity<Article> articleEntity = restTemplate.getForEntity(
					RequestProvider.GET_ARTICLE_URL, Article.class, urlVariables);
			//map request data to business objects

			Article article = new Article();
			article.setBarcode(articleEntity.getBody().getBarcode());
			article.setName(articleEntity.getBody().getName());
			article.setPrice(articleEntity.getBody().getPrice());
			return article;
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}


}
