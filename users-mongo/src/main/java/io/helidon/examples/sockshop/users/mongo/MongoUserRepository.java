package io.helidon.examples.sockshop.users.mongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import io.helidon.examples.sockshop.users.Address;
import io.helidon.examples.sockshop.users.Card;
import io.helidon.examples.sockshop.users.DefaultUserRepository;
import io.helidon.examples.sockshop.users.User;
import io.helidon.examples.sockshop.users.UserRepository;

import com.mongodb.client.MongoCollection;

import static com.mongodb.client.model.Filters.eq;

/**
 *
 */
@ApplicationScoped
@Specializes
public class MongoUserRepository extends DefaultUserRepository {
    @Inject
    private MongoCollection<MongoUser> users;

    @Override
    public Address.Id addAddress(String userID, Address address) {
        MongoUser user = findUser(userID);
        if (user != null) {
            Address.Id id = user.addAddress(address).getId();
            updateUser(userID, user);
            return id;
        }
        return null;
    }

    @Override
    public Address getAddress(Address.Id id) {
        return findUser(id.getCustomerId()).getAddress(id);
    }

    @Override
    public void removeAddress(Address.Id id) {
        String    userID = id.getCustomerId();
        MongoUser user   = findUser(userID);
        if (user != null) {
            user.removeAddress(id);
            updateUser(userID, user);
        }
    }

    @Override
    public Card.Id addCard(String userID, Card card) {
        MongoUser user = findUser(userID);
        if (user != null) {
            Card.Id id = user.addCard(card).getId();
            updateUser(userID, user);
            return id;
        }
        return null;
    }

    @Override
    public Card getCard(Card.Id id) {
        return findUser(id.getCustomerId()).getCard(id);
    }

    @Override
    public void removeCard(Card.Id id) {
        String    userID = id.getCustomerId();
        MongoUser user   = findUser(userID);
        if (user != null) {
            user.removeCard(id);
            updateUser(userID, user);
        }
    }

    @Override
    public Collection<? extends User> getAllUsers() {
        List<MongoUser> results = new ArrayList<>();
        users.find().forEach((Consumer<? super MongoUser>) results::add);
        return results;
    }

    @Override
    public User getOrCreate(String id) {
        return Optional.ofNullable(findUser(id))
                .orElse(new MongoUser());
    }

    @Override
    public User getUser(String id) {
        return findUser(id);
    }

    @Override
    public User removeUser(String id) {
        MongoUser user = findUser(id);
        if (user != null) {
            users.deleteOne(eq("username", id));
        }
        return user;
    }

    @Override
    public boolean authenticate(String username, String password) {
        User user = findUser(username);
        return user != null ? user.authenticate(password) : false;
    }

    @Override
    public User register(User user) {
        User existing = findUser(user.getUsername());
        if (existing == null) {
            users.insertOne(new MongoUser(user));
        }
        return existing;
    }

    // --- helpers ----------------------------------------------------------

    private MongoUser findUser(String userID) {
        return users.find(eq("username", userID)).first();
    }

    private void updateUser(String userID, MongoUser user) {
        users.replaceOne(eq("username", userID), user);
    }
}
