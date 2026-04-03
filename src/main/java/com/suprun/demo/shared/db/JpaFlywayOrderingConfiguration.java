package com.suprun.demo.shared.db;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * Ensures Flyway migrations run before Hibernate schema validation.
 *
 * In some Spring Boot 4 setups the default ordering can validate JPA before Flyway executes,
 * which breaks "ddl-auto=validate" on a fresh database.
 */
@Configuration(proxyBeanMethods = false)
public class JpaFlywayOrderingConfiguration {

    @Bean
    static BeanFactoryPostProcessor flywayBeforeJpaPostProcessor() {
        return new FlywayBeforeJpaPostProcessor();
    }

    static final class FlywayBeforeJpaPostProcessor implements BeanFactoryPostProcessor {

        private static final String FLYWAY_INITIALIZER = "flywayInitializer";
        private static final String FLYWAY = "flyway";
        private static final String ENTITY_MANAGER_FACTORY = "entityManagerFactory";

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            if (!(beanFactory instanceof BeanDefinitionRegistry registry)) {
                return;
            }
            if (!registry.containsBeanDefinition(FLYWAY_INITIALIZER) || !registry.containsBeanDefinition(ENTITY_MANAGER_FACTORY)) {
                return;
            }

            // Ensure Flyway doesn't depend on JPA (otherwise JPA validation triggers first on a fresh DB).
            if (registry.containsBeanDefinition(FLYWAY)) {
                BeanDefinition flyway = registry.getBeanDefinition(FLYWAY);
                flyway.setDependsOn(removeFromDependsOn(flyway.getDependsOn(), ENTITY_MANAGER_FACTORY));
            }

            BeanDefinition flywayInit = registry.getBeanDefinition(FLYWAY_INITIALIZER);
            flywayInit.setDependsOn(removeFromDependsOn(flywayInit.getDependsOn(), ENTITY_MANAGER_FACTORY));

            // Ensure JPA depends on Flyway.
            BeanDefinition emf = registry.getBeanDefinition(ENTITY_MANAGER_FACTORY);
            emf.setDependsOn(addToDependsOn(emf.getDependsOn(), FLYWAY_INITIALIZER));
        }

        private static String[] addToDependsOn(String[] existing, String beanName) {
            if (existing != null && Arrays.asList(existing).contains(beanName)) {
                return existing;
            }
            LinkedHashSet<String> dependsOn = new LinkedHashSet<>();
            dependsOn.add(beanName);
            if (existing != null) {
                dependsOn.addAll(Arrays.asList(existing));
            }
            return dependsOn.toArray(String[]::new);
        }

        private static String[] removeFromDependsOn(String[] existing, String beanName) {
            if (existing == null || existing.length == 0) {
                return existing;
            }
            LinkedHashSet<String> dependsOn = new LinkedHashSet<>(Arrays.asList(existing));
            dependsOn.remove(beanName);
            return dependsOn.toArray(String[]::new);
        }
    }
}
