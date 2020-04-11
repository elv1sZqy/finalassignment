package xzy.lovelybj.finalassignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinalassignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinalassignmentApplication.class, args);
    }

    /**
     * 这个注入删掉 bboss 的配置就无法生效了
     */
  /*  @Autowired
    private BBossESStarter bbossESStarter;*/
}
