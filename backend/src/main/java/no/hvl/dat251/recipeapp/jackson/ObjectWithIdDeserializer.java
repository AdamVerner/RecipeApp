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
import lombok.extern.slf4j.Slf4j;
import no.hvl.dat251.recipeapp.common.ApplicationContextProvider;
import no.hvl.dat251.recipeapp.domain.ObjectWithId;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
public class ObjectWithIdDeserializer<T extends ObjectWithId> extends StdDeserializer<T> implements ContextualDeserializer {

    private final Class<T> clazz;

    public ObjectWithIdDeserializer() {
        this(null);
    }

    public ObjectWithIdDeserializer(Class<T> clazz) {
        super(clazz);
        this.clazz = clazz;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) {
        final JavaType type = beanProperty == null ? deserializationContext.getContextualType() : beanProperty.getType();
        Class<?> c = type.isContainerType() ? type.getContentType().getRawClass() : type.getRawClass();
        return new ObjectWithIdDeserializer<>((Class<T>) c);
    }

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        if(jsonParser.getCurrentToken() == JsonToken.VALUE_NULL) {
            return null;
        } else {
            Integer id = jsonParser.readValueAs(Integer.class);

            log.debug("Deserializing " + clazz.getSimpleName() + " with ID " + id);
            return getObjectWithId(id);
        }
    }

    @Override
    public Object deserializeWithType(JsonParser jsonParser, DeserializationContext deserializationContext, TypeDeserializer typeDeserializer) throws IOException {
        return deserialize(jsonParser, deserializationContext);
    }

    @Transactional(rollbackFor = Exception.class)
    public T getObjectWithId(Integer id) {
        return ApplicationContextProvider.getSessionFactory().openSession().get(clazz, id);
    }

}
