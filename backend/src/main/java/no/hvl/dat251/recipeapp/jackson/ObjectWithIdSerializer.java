package no.hvl.dat251.recipeapp.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import no.hvl.dat251.recipeapp.domain.ObjectWithId;

import java.io.IOException;

public class ObjectWithIdSerializer extends JsonSerializer<ObjectWithId> {

    @Override
    public void serializeWithType(ObjectWithId value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        gen.writeObject(value.getId());
    }

    @Override
    public void serialize(ObjectWithId objectWithId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(objectWithId.getId());
    }

}

