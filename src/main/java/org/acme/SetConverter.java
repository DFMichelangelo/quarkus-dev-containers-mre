package org.acme;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.persistence.AttributeConverter;
import lombok.SneakyThrows;

import java.util.Set;

public class SetConverter implements AttributeConverter<Set<String>, String> {
    private final TypeReference<Set<String>> typeReference = new TypeReference<>() {};

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(Set<String> attribute) {
        return JsonMapper.builder().build().writer().withDefaultPrettyPrinter().writeValueAsString(attribute);
    }

    @SneakyThrows
    @Override
    public Set<String> convertToEntityAttribute(String dbData) {
        return JsonMapper.builder().build().readValue(dbData, typeReference);
    }
}
