package xzy.lovelybj.finalassignment.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author zhuQiYun
 * @create 2019/12/2
 * @description :
 */
@Configuration
public class ElasticSearchConfig {

    @Autowired
    private ElasticSearchProperty elasticSearchProperty;


    @Bean(name = "highLevelClient",destroyMethod="close")//这个close是调用RestHighLevelClient中的close
    @Scope("singleton")
    public RestHighLevelClient getHighLevelClient() {
        final CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(elasticSearchProperty.getUserName(), elasticSearchProperty.getPassword()));
        HttpHost[] httpHosts = new HttpHost[elasticSearchProperty.getHosts().size()];
        for (int i = 0; i<httpHosts.length;i++){
            Hosts host = elasticSearchProperty.getHosts().get(i);
            httpHosts[i] = new HttpHost(host.getIp(), host.getPort());
        }

        RestClientBuilder builder = RestClient.builder(httpHosts)
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));

        return new RestHighLevelClient(builder);

    }


}
