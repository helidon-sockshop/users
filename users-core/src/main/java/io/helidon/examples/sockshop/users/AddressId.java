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

import javax.json.bind.adapter.JsonbAdapter;
import javax.json.bind.annotation.JsonbTypeAdapter;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Composite key for the {@link Address class} when using JPA.
 */
@JsonbTypeAdapter(AddressId.JsonAdapter.class)
@Data
public class AddressId implements Serializable {
    /**
     * The customer Id that the address is associated with.
     */
    private String user;

    /**
     * The id for the address.
     */
    private String addressId;

    @Builder
    public AddressId(String id) {
        String[] parts = id.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Address Id is in the wrong format");
        }
        user = parts[0];
        addressId = parts[1];
    }

    /**
     * Default constructor.
     */
    public AddressId() {}

    /**
     * Construct an instance of {@code AddressId} with the specified parameters.
     */
    public AddressId(String user, String addressId) {
        this.user = user;
        this.addressId = addressId;
    }

    @Override
    public String toString() {
        return user + ":" + addressId;
    }

    public static class JsonAdapter implements JsonbAdapter<AddressId, String> {
        @Override
        public String adaptToJson(AddressId id) throws Exception {
            return id.toString();
        }

        @Override
        public AddressId adaptFromJson(String id) throws Exception {
            return new AddressId(id);
        }
    }
}
