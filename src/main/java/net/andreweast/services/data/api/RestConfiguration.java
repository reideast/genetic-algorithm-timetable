package net.andreweast.services.data.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Type;

@Configuration
public class RestConfiguration implements RepositoryRestConfigurer {
    // How to serialise IDs for all entity classes in JSON for REST api
    // See: https://stackoverflow.com/a/49344988/5271224
    @Autowired
    private EntityManager entityManager;
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(
                entityManager.getMetamodel().getEntities()
                        .stream().map(Type::getJavaType).toArray(Class[]::new)
        );
    }
}
