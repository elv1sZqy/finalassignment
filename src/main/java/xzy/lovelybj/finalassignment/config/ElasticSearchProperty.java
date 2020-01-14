package xzy.lovelybj.finalassignment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author zhuQiYun
 * @create 2019/12/2
 * @description :
 */
@Data
@ConfigurationProperties(prefix = "elasticsearch")
@Configuration
public class ElasticSearchProperty {

    @NestedConfigurationProperty
    private List<Hosts> hosts;


    private String userName;


    private String password;


}
