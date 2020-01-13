package io.helidon.examples.sockshop.users;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;

public class User implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private Map<Address.Id, Address> addresses = new LinkedHashMap<>();
    private Map<Card.Id, Card> cards = new LinkedHashMap<>();

    private int nextAddressId = 1;

    public User() {
    }

    public User(String firstName, String lastName, String email, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return username;
    }

    public void setId(String id) {
        this.username = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonbTransient
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonbTransient
    public Collection<Address> getAddresses() {
        return addresses.values();
    }

    public Address getAddress(Address.Id id) {
        return addresses.getOrDefault(id, new Address());
    }

    public Address addAddress(Address address) {
        if (address.getId() == null) {
            address.setId(new Address.Id(getId(), nextAddressId++));
        }
        addresses.put(address.getId(), address);
        return address;
    }

    public User removeAddress(Address.Id id) {
        addresses.remove(id);
        return this;
    }

    @JsonbTransient
    public Collection<Card> getCards() {
        return cards.values();
    }

    public Card getCard(Card.Id id) {
        return cards.getOrDefault(id, new Card());
    }

    public Card addCard(Card card) {
        if (card.getId() == null) {
            card.setId(new Card.Id(getId(), card.last4()));
        }
        cards.put(card.getId(), card);
        return card;
    }

    public User removeCard(Card.Id id) {
        cards.remove(id);
        return this;
    }

    @JsonbProperty("_links")
    public Links getLinks() {
        return Links.customer(getId());
    }

    public Boolean authenticate(String password) {
        return password.equals(this.password);
    }
}
