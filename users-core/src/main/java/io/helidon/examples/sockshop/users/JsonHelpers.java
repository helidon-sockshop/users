package io.helidon.examples.sockshop.users;

import java.util.Map;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;

import static java.util.Collections.singletonMap;

abstract class JsonHelpers {
    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(null);

    static JsonObjectBuilder obj() {
        return JSON.createObjectBuilder();
    }

    static Map<String, Object> embed(String name, Object value) {
        return singletonMap("_embedded", singletonMap(name, value));
    }
}
