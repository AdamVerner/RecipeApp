package no.hvl.dat251.recipeapp.common;

import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext context;
    private static SessionFactory sessionFactory;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
        sessionFactory = applicationContext.getBean(SessionFactory.class);
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
