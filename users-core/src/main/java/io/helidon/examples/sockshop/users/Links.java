/*
 *  Copyright (c) 2020 Oracle and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.helidon.examples.sockshop.users;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import io.helidon.common.CollectionsHelper;

public class Links extends LinkedHashMap<String, Links.Link> implements Serializable {
    private static Map<String, String> ENTITY_MAP = CollectionsHelper.mapOf("customer", "customers",
                                                                            "address", "addresses",
                                                                            "card", "cards");

    private Links addLink(String entity, String id) {
        Link link = Link.to(ENTITY_MAP.get(entity), id);
        put(entity, link);
        put("self", link);
        return this;
    }

    private Links addAttrLink(String entity, String id, String attr) {
        Link link = Link.to(ENTITY_MAP.get(entity), id, attr);
        put(attr, link);
        return this;
    }

    public static Links customer(String id) {
        return new Links()
            .addLink("customer", id)
            .addAttrLink("customer", id, "addresses")
            .addAttrLink("customer", id, "cards");
    }

    public static Links address(AddressId id) {
        return new Links().addLink("address", id.toString());
    }

    public static Links card(CardId id) {
        return new Links().addLink("card", id.toString());
    }

    public static class Link implements Serializable {
        public String href;

        public Link() {
        }

        Link(String href) {
            this.href = href;
        }

        static Link to(Object... pathElements) {
            StringBuilder sb = new StringBuilder("http://user");
            for (Object e : pathElements) {
                sb.append('/').append(e);
            }
            return new Link(sb.toString());
        }
    }
}
