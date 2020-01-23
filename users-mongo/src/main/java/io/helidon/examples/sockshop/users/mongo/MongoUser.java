package io.helidon.examples.sockshop.users.mongo;

import java.util.Collection;

import javax.json.bind.annotation.JsonbTransient;

import io.helidon.examples.sockshop.users.Address;
import io.helidon.examples.sockshop.users.Card;
import io.helidon.examples.sockshop.users.User;

import org.bson.types.ObjectId;

/**
 * @author Aleksandar Seovic  2020.01.22
 */
public class MongoUser extends User {
    @JsonbTransient
    public ObjectId _id;

    public MongoUser() {
    }

    MongoUser(User user) {
        super(user.getFirstName(),
              user.getLastName(),
              user.getEmail(),
              user.getUsername(),
              user.getPassword(),
              user.getAddresses(),
              user.getCards());
    }

    public void setAddresses(Collection<Address> addresses) {
        addresses.forEach(this::addAddress);
    }

    public void setCards(Collection<Card> cards) {
        cards.forEach(this::addCard);
    }
}
