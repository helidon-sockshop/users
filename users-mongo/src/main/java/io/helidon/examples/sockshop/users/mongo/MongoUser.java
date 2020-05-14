package io.helidon.examples.sockshop.users.mongo;

import java.util.Collection;

import javax.json.bind.annotation.JsonbTransient;

import io.helidon.examples.sockshop.users.Address;
import io.helidon.examples.sockshop.users.Card;
import io.helidon.examples.sockshop.users.User;

import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

/**
 * @author Aleksandar Seovic  2020.01.22
 */
/**
 * MongoDB treats {@link User#getUsername()} ()} as object ID, which causes errors.
 * <p/>
 * We need to define an {@code ObjectId _id} field explicitly to avoid
 * that.
 */
@NoArgsConstructor
public class MongoUser extends User {
    /**
     * Just to make MongoDB happy.
     */
    @JsonbTransient
    public ObjectId _id;

    MongoUser(User user) {
        super(user.getFirstName(),
              user.getLastName(),
              user.getEmail(),
              user.getUsername(),
              user.getPassword(),
              user.getAddresses(),
              user.getCards());
    }

    /**
     * Set the user addresses from the specified addresses.
     *
     * @param addresses  the addresses to be set
     */
    public void setAddresses(Collection<Address> addresses) {
            addresses.forEach(this::addAddress);
        }

    /**
     * Set the user cards from the specified cards.
     *
     * @param cards the cards to be set
     */
    public void setCards(Collection<Card> cards) {
            cards.forEach(this::addCard);
        }
}
