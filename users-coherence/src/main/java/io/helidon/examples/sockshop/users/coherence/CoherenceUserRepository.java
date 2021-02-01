/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.users.coherence;

import io.helidon.examples.sockshop.users.Address;
import io.helidon.examples.sockshop.users.AddressId;
import io.helidon.examples.sockshop.users.Card;
import io.helidon.examples.sockshop.users.CardId;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import io.helidon.examples.sockshop.users.User;
import io.helidon.examples.sockshop.users.DefaultUserRepository;

import com.oracle.coherence.cdi.Name;
import com.tangosol.net.NamedMap;
import org.eclipse.microprofile.opentracing.Traced;

import static javax.interceptor.Interceptor.Priority.APPLICATION;

/**
 * An implementation of {@link io.helidon.examples.sockshop.users.UserRepository}
 * that that uses Coherence as a backend data store.
 */
@ApplicationScoped
@Alternative
@Priority(APPLICATION)
@Traced
public class CoherenceUserRepository extends DefaultUserRepository {

    protected NamedMap<String, User> users;

    @Inject
    public CoherenceUserRepository(@Name("users") NamedMap<String, User> users) {
        super(users);
        this.users = users;
    }

    @Override
    public AddressId addAddress(String userID, Address address) {
        return users.invoke(userID, entry -> {
            User u = entry.getValue(new User(entry.getKey()));
            Address addr = u.addAddress(address);
            entry.setValue(u);
            return addr.getId();
        });
    }

    @Override
    public void removeAddress(AddressId id) {
        String userID = id.getUser();
        users.invoke(userID, entry -> {
            User u = entry.getValue(new User(entry.getKey()));
            entry.setValue(u.removeAddress(id.getAddressId()));
            return null;
        });
    }

    @Override
    public CardId addCard(String userID, Card card) {
        return users.invoke(userID, entry -> {
            User u = entry.getValue(new User(entry.getKey()));
            Card c = u.addCard(card);
            entry.setValue(u);
            return c.getId();
        });
    }

    @Override
    public void removeCard(CardId id) {
        String userId = id.getUser();
        users.invoke(userId, entry -> {
            User u = entry.getValue(new User(entry.getKey()));
            entry.setValue(u.removeCard(id.getCardId()));
            return null;
        });
    }

    @Override
    public boolean authenticate(String username, String password) {
        return users.invoke(username, entry -> {
            User u = entry.getValue(new User(entry.getKey()));
            return u.authenticate(password);
        });
    }
}
