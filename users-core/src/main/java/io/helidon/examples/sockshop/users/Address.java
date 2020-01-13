package io.helidon.examples.sockshop.users;

import java.io.Serializable;
import java.util.Objects;

import javax.json.bind.adapter.JsonbAdapter;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTypeAdapter;

public class Address implements Serializable {
    private Id id;
    private String number;
    private String street;
    private String city;
    private String postcode;
    private String country;

    public Address() {
    }

    public Address(String number, String street, String city, String postcode, String country) {
        this.number = number;
        this.street = street;
        this.city = city;
        this.postcode = postcode;
        this.country = country;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    @JsonbProperty("_links")
    public Links getLinks() {
        return id != null
                ? Links.address(id.toString())
                : Links.address("");
    }

    @JsonbTypeAdapter(Id.JsonAdapter.class)
    public static class Id implements Serializable {
        private String customerId;
        private String addressId;

        public Id() {
        }

        public Id(String id) {
            String[] parts = id.split(":");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Address Id is in the wrong format");
            }
            customerId = parts[0];
            addressId = parts[1];
        }

        public Id(String customerId, int addressId) {
            this.customerId = customerId;
            this.addressId = Integer.toString(addressId);
        }

        public String getCustomerId() {
            return customerId;
        }

        public String getAddressId() {
            return addressId;
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
