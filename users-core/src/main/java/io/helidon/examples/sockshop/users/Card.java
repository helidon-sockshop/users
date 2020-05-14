package io.helidon.examples.sockshop.users;

import java.io.Serializable;
import java.util.Objects;

import javax.json.bind.adapter.JsonbAdapter;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.persistence.Embeddable;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class Card implements Serializable {
    /**
     * The card id.
     */
    private Id id;

    /**
     * The card number.
     */
    private String longNum;

    /**
     * The expiration date.
     */
    private String expires;

    /**
     * The security code.
     */
    private String ccv;

    @Builder
    public Card(String longNum, String expires, String ccv) {
        this.longNum = longNum;
        this.expires = expires;
        this.ccv = ccv;
    }

    /**
     * Return the card with masked card number.
     *
     * @return the card with masked card number
     */
    public Card mask() {
        if (longNum != null) {
            int len = longNum.length() - 4;
            longNum = "****************".substring(0, len) + longNum.substring(len);
        }
        return this;
    }

    /**
     * Return the last 4 digit of the card number.
     *
     * @return the last 4 digit of the card number
     */
    public String last4() {
        return longNum == null ? null : longNum.substring(longNum.length() - 4);
    }

    @JsonbProperty("_links")
    public Links getLinks() {
        return id != null
                ? Links.card(id.toString())
                : Links.card("");
    }

    /**
     * Card Id class.
     */
    @JsonbTypeAdapter(Id.JsonAdapter.class)
    public static class Id implements Serializable {
        /**
         * The ID of the customer this card belongs to.
         */
        private String customerId;

        /**
         * The first part of the card id.
         */
        private String cardId;

        @Builder
        public Id(String id) {
                String[] parts = id.split(":");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Card Id is in the wrong format");
                }
                customerId = parts[0];
                cardId = parts[1];
            }

            @Builder
            public Id(String customerId, String cardId) {
                this.customerId = customerId;
                this.cardId = cardId;
            }

        /**
         * Return the customer id.
         *
         * @return the customer id
         */
        public String getCustomerId() {
            return customerId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Id id = (Id) o;
            return customerId.equals(id.customerId) &&
                    cardId.equals(id.cardId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(customerId, cardId);
        }

        @Override
        public String toString() {
            return customerId + ":" + cardId;
        }

        public static class JsonAdapter implements JsonbAdapter<Id, String> {
            @Override
            public String adaptToJson(Id id) throws Exception {
                return id.toString();
            }

            @Override
            public Id adaptFromJson(String id) throws Exception {
                return new Id(id);
            }
        }
    }
}
