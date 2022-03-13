package no.hvl.dat251.recipeapp.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import no.hvl.dat251.recipeapp.domain.ObjectWithId;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;

public class ObjectWithIdDeserializer<OBJECT extends ObjectWithId> extends StdDeserializer<OBJECT> implements ContextualDeserializer, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private final Class<OBJECT> clazz;

    public ObjectWithIdDeserializer() {
        this(null);
    }

    public ObjectWithIdDeserializer(Class<OBJECT> clazz) {
        super(clazz);
        this.clazz = clazz;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) {
        final JavaType type = beanProperty == null ? deserializationContext.getContextualType() : beanProperty.getType();
        Class<?> c = type.isContainerType() ? type.getContentType().getRawClass() : type.getRawClass();
        return new ObjectWithIdDeserializer<>((Class<OBJECT>) c);
    }

    @Override
    public OBJECT deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        if(jsonParser.getCurrentToken() == JsonToken.VALUE_NULL) {
            return null;
        } else {
            Integer id = jsonParser.readValueAs(Integer.class);
            return applicationContext.getBean(SessionFactory.class).getCurrentSession().get(clazz, id);
        }
    }

    @Override
    public Object deserializeWithType(JsonParser jsonParser, DeserializationContext deserializationContext, TypeDeserializer typeDeserializer) throws IOException {
        return deserialize(jsonParser, deserializationContext);
    }

}
