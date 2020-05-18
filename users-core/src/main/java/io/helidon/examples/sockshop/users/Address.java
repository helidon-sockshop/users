package io.helidon.examples.sockshop.users;

import java.io.Serializable;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Representation of an address.
 */
@Data
@NoArgsConstructor
@Entity
@IdClass(AddressId.class)
@Schema(description = "User address")
public class Address implements Serializable {
    /**
     * The address identifier.
     */
    @Id
    @Schema(description = "The address identifier")
    private String addressId;

    /**
     * The street number.
     */
    @Schema(description = "The street number")
    private String number;

    /**
     * The street name.
     */
    @Schema(description = "The street name")
    private String street;

    /**
     * The city name.
     */
    @Schema(description = "The city name")
    private String city;

    /**
     * The postal code.
     */
    @Schema(description = "The postal code")
    private String postcode;

    /**
     * The country name.
     */
    @Schema(description = "The country name")
    private String country;

    /**
     * The Address.Id.
     */
    private AddressId id;

    /**
     * The user this address is associated with, purely for JPA optimization.
     */
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonbTransient
    private User user;

    /**
     * Construct {@code Address} with specified parameters.
     */
    public Address(String number, String street, String city, String postcode, String country) {
        this.number = number;
        this.street = street;
        this.city = city;
        this.postcode = postcode;
        this.country = country;
    }

    /**
     * Return the user this address is associated with.
     *
     * @return the user this address is associated with
     */
    User getUser() {
    return user;
    }

    /**
     * Set the uer this address belongs to.
     *
     * @param user the user to set
     *
     * @return this user
     */
    Address setUser(User user) {
        this.user = user;
        return this;
    }

    /**
     * Set the address id.
     */
    Address setAddressId(String id) {
        this.addressId = id;
        return this;
    }

    /**
     * Return Address.Id for this address.
     */
    public AddressId getId() {
        if (id == null) {
            id = new AddressId(user.getUsername(), addressId);
        }
        return id;
    }
}
