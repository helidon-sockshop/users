package io.helidon.examples.sockshop.users;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User information.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {
    /**
     * User identifier.
     */
    @Id
    private String username;

    /**
     * The first name.
     */
    private String firstName;

    /**
     * The last name.
     */
    private String lastName;

    /**
     * The email.
     */
    private String email;

    /**
     * The password.
     */
    private String password;

    /**
     * A map of addresses that are associated to the user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<Address.Id, Address> addresses = new LinkedHashMap<>();

    /**
     * A map of cards that belongs to the user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<Card.Id, Card> cards = new LinkedHashMap<>();

    /**
     * Construct {@code User} with specified attributes.
     */
    public User(String firstName, String lastName, String email, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    /**
     * Construct {@code User} with specified attributes.
     */
    protected User(String firstName, String lastName, String email, String username, String password,
                Collection<Address> addresses, Collection<Card> cards) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        addresses.forEach(this::addAddress);
        cards.forEach(this::addCard);
    }

    /**
     * Return all the addresses associated to the user.
     *
     * @return all the addresses associated to the user
     */
    @JsonbTransient
    public Collection<Address> getAddresses() {
        return addresses.values();
    }

    /**
     * Return the address for the specified address ID.
     *
     * @param id the address ID
     *
     * @return the address for the specified address ID
     */
    public Address getAddress(Address.Id id) {
            return addresses.getOrDefault(id, new Address());
        }

    /**
     * Add the specified address.
     *
     * @param address to be added
     *
     * @return the added address
     */
    public Address addAddress(Address address) {
            if (address.getId() == null) {
                address.setId(new Address.Id(username, addresses.size() + 1));
            }
            addresses.put(address.getId(), address);
            return address;
        }

    /**
     * Remove address for the specified address ID.
     *
     * @param id the address ID
     *
     * @return the user
     */
        public User removeAddress(Address.Id id) {
            addresses.remove(id);
            return this;
        }

    /**
     * Return all cards associated with the user.
     *
     * @return all cards for the user
     */
    @JsonbTransient
    public Collection<Card> getCards() {
        return cards.values();
    }

    /**
     * Return the card for the specified card ID.
     *
     * @param id the card ID
     *
     * @return the card for the specified card ID
     */
    public Card getCard(Card.Id id) {
        return cards.getOrDefault(id, new Card());
    }

    /**
     * Add the specified card to the user.
     *
     * @param card the card to be added
     *
     * @return the added card
     */
    public Card addCard(Card card) {
        if (card.getId() == null) {
            card.setId(new Card.Id(username, card.last4()));
        }
        cards.put(card.getId(), card);
        return card;
    }

    /**
     * Remove the card with the specified card ID.
     *
     * @param id the card ID
     *
     * @return the user
     */
    public User removeCard(Card.Id id) {
        cards.remove(id);
        return this;
    }

    @JsonbProperty("_links")
    public Links getLinks() {
        return Links.customer(username);
    }

    /**
     * Authenticate the user against the specified password.
     *
     * @param password the password
     *
     * @return true if the specified password match the user password
     */
    public Boolean authenticate(String password) {
        return password.equals(this.password);
    }
}
