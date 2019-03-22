package net.andreweast;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Fetch any Spring Boot @Component (i.e. @Service, @Repository, or @Controller) without injection via @Autowire
 * This code is by Naresh Joshi
 * Article: https://www.programmingmitra.com/2017/03/AutoWiring-Spring-Beans-Into-Classes-Not-Managed-By-Spring-Like-JPA-Entity-Listeners.html
 * Code: https://github.com/njnareshjoshi/articles/tree/master/spring-data-jpa-auditing
 */
@Service
public class BeanUtil implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }
}
