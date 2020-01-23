package io.helidon.examples.sockshop.users;

import java.io.Serializable;
import java.util.Objects;

import javax.json.bind.adapter.JsonbAdapter;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTypeAdapter;

public class Card implements Serializable {
    private Id id;
    private String longNum;
    private String expires;
    private String ccv;

    public Card() {
    }

    public Card(String longNum, String expires, String ccv) {
        this.longNum = longNum;
        this.expires = expires;
        this.ccv = ccv;
    }

    public Card mask() {
        if (longNum != null) {
            int len = longNum.length() - 4;
            longNum = "****************".substring(0, len) + longNum.substring(len);
        }
        return this;
    }

    public String last4() {
        return longNum == null ? null : longNum.substring(longNum.length() - 4);
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public String getLongNum() {
        return longNum;
    }

    public void setLongNum(String longNum) {
        this.longNum = longNum;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getCcv() {
        return ccv;
    }

    public void setCcv(String ccv) {
        this.ccv = ccv;
    }

    @JsonbProperty("_links")
    public Links getLinks() {
        return id != null
                ? Links.card(id.toString())
                : Links.card("");
    }

    @JsonbTypeAdapter(Id.JsonAdapter.class)
    public static class Id implements Serializable {
        private String customerId;
        private String cardId;

        public Id() {
        }

        public Id(String id) {
            String[] parts = id.split(":");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Card Id is in the wrong format");
            }
            customerId = parts[0];
            cardId = parts[1];
        }

        public Id(String customerId, String cardId) {
            this.customerId = customerId;
            this.cardId = cardId;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getCardId() {
            return cardId;
        }

        public void setCardId(String cardId) {
            this.cardId = cardId;
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
