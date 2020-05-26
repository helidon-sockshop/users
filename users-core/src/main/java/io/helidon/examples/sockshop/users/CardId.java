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
 * Composite key for the {@link Card class} when using JPA.
 */
@JsonbTypeAdapter(CardId.JsonAdapter.class)
@Data
public class CardId implements Serializable {
    /**
     * The ID of the customer to whom the card belongs.
     */
    private String user;

    /**
     * The card id.
     */
    private String cardId;

    /**
     * Default constructor.
     */
    public CardId() {}

    @Builder
    public CardId(String id) {
        String[] parts = id.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Card Id is in the wrong format");
        }
        user = parts[0];
        cardId = parts[1];
    }

    /**
     * Construct {@code CardId} with specified parameters.
     */
    public CardId(String customerId, String cardId) {
        this.user = customerId;
        this.cardId = cardId;
    }

    @Override
    public String toString() {
        return user + ":" + cardId;
    }

    public static class JsonAdapter implements JsonbAdapter<CardId, String> {
        @Override
        public String adaptToJson(CardId id) throws Exception {
            return id.toString();
        }

        @Override
        public CardId adaptFromJson(String id) throws Exception {
            return new CardId(id);
        }
    }
}
