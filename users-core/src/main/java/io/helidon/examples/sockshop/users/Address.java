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

/**
 * User address.
 */
@Data
@NoArgsConstructor
@Embeddable
public class Address implements Serializable {
    /**
     * The address ID.
     */
    private Id id;

    /**
     * The address number.
     */
    private String number;

    /**
     * The street name.
     */
    private String street;

    /**
     * The city name.
     */
    private String city;

    /**
     * The post code.
     */
    private String postcode;

    /**
     * The country name.
     */
    private String country;

    /**
     * Construct {@code Address} with specified attributes.
     */
    public Address(String number, String street, String city, String postcode, String country) {
        this.number = number;
        this.street = street;
        this.city = city;
        this.postcode = postcode;
        this.country = country;
    }

    @JsonbProperty("_links")
    public Links getLinks() {
        return id != null
                ? Links.address(id.toString())
                : Links.address("");
    }

    /**
     * Address Id class.
     */
    @JsonbTypeAdapter(Id.JsonAdapter.class)
    public static class Id implements Serializable {
        /**
         * The customer Id that the address associates to.
         */
        private String customerId;

        /**
         * The id for the address.
         */
        private String addressId;

        @Builder
        public Id(String id) {
            String[] parts = id.split(":");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Address Id is in the wrong format");
            }
            customerId = parts[0];
            addressId = parts[1];
        }

        /**
         * Construct a {@code Address.Id} with the specified attributes.
         */
        public Id(String customerId, int addressId) {
            this.customerId = customerId;
            this.addressId = Integer.toString(addressId);
        }

        /**
         * Return the customer id that the address is associated to.
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
                    addressId.equals(id.addressId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(customerId, addressId);
        }

        @Override
        public String toString() {
            return customerId + ":" + addressId;
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
