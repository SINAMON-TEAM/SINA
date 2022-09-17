package mon.sinamon;;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*");
               /* .allowedOrigins("http://172.30.7.190:5500")
                .allowedHeaders("*")
                .allowedMethods("*")
                .exposedHeaders("Set-Cookie")
                .allowCredentials(true);*/
    }
}
