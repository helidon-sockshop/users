/*
 *  Copyright (c) 2020 Oracle and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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

import com.oracle.coherence.cdi.Cache;
import com.tangosol.net.NamedCache;
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

    protected NamedCache<String, User> users;

    @Inject
    public CoherenceUserRepository(@Cache("users") NamedCache<String, User> users) {
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
