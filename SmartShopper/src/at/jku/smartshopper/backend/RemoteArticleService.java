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
		
//		Article art = new Article();
//		art.setBarcode("123456");
//		art.setName("Binder 44/21");
//		art.setPrice(12.33);
//		return art;
		try {
			DisableSSLCertificateCheckUtil.disableChecks();

			RequestProvider provider = new RequestProvider();
			HttpHeaders header = provider.getHttpHeader();

			// HttpEntity<String> requestEntity = new
			// HttpEntity<String>(username, header);

			RestTemplate restTemplate = provider.getRestTemplate();

			Map<String, Object> urlVariables = new HashMap<String, Object>();
			urlVariables.put("barcode", barcode);

			// restTemplate.get(RequestProvider.GET_USER_URL, requestEntity,
			// urlVariables);
			ResponseEntity<Article> articleEntity = restTemplate.getForEntity(
					RequestProvider.GET_ARTICLE_URL, Article.class, urlVariables);
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
