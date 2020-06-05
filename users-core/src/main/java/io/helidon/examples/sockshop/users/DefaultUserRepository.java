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

package io.helidon.examples.sockshop.users;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.opentracing.Traced;

import static javax.interceptor.Interceptor.Priority.APPLICATION;

/**
 * Simple in-memory implementation of {@link io.helidon.examples.sockshop.users.UserRepository}
 * that can be used for demos and testing.
 * <p/>
 * This implementation is obviously not suitable for production use, because it is not
 * persistent and it doesn't scale, but it is trivial to write and very useful for the
 * API testing and quick demos.
 */
@ApplicationScoped
@Traced
@Priority(APPLICATION - 10)
public class DefaultUserRepository implements UserRepository {

    protected final Map<String, User> users;

    /**
     * Construct {@code DefaultUserRepository} with empty storage map.
     */
    public DefaultUserRepository()  {
    this(new ConcurrentHashMap<>());
    }

    /**
     * Construct {@code DefaultUserRepository} with specified storage map.
     */
    public DefaultUserRepository(Map<String, User> users) {
            this.users = users;
        }

    @Override
    public AddressId addAddress(String userID, Address address) {
        User user = getOrCreate(userID);
        AddressId id = user.addAddress(address).getId();
        saveUser(user);
        return id;
    }

    @Override
    public Address getAddress(AddressId id) {
        return getOrCreate(id.getUser()).getAddress(id.getAddressId());
    }

    @Override
    public void removeAddress(AddressId id) {
        User user = getOrCreate(id.getUser());
        user.removeAddress(id.getAddressId());
        saveUser(user);
    }

    @Override
    public CardId addCard(String userID, Card card) {
        User user = getOrCreate(userID);
        CardId id = user.addCard(card).getId();
        saveUser(user);
        return id;
    }

    @Override
    public Card getCard(CardId id) {
        return getOrCreate(id.getUser()).getCard(id.getCardId());
    }

    @Override
    public void removeCard(CardId id) {
        User user = getOrCreate(id.getUser());
        user.removeCard(id.getCardId());
        saveUser(user);
    }

    @Override
    public Collection<? extends User> getAllUsers() {
        return users.values();
    }

    @Override
    public User getOrCreate(String id) {
        return users.getOrDefault(id, new User(id));
    }

    @Override
    public User getUser(String id) {
        return users.get(id);
    }

    @Override
    public User removeUser(String id) {
        return users.remove(id);
    }

    @Override
    public boolean authenticate(String username, String password) {
        User user = users.get(username);
        return user != null ? user.authenticate(password) : false;
    }

    @Override
    public User register(User user) {
        return users.putIfAbsent(user.getUsername(), user);
    }

    // ---- helpers ---------------------------------------------------------

    private void saveUser(User user) {
        users.put(user.getUsername(), user);
    }

    /**
     * Load test data into this repository.
     */
    @PostConstruct
    void createTestUsers() {
        User user = new User("Test", "User", "user@weavesocks.com", "user", "pass");
        user.addCard(new Card("1234123412341234", "12/19", "123"));
        user.addAddress(new Address("123", "Main St", "Springfield", "12123", "USA"));
        register(user);

        User aleks = new User("Aleks", "Seovic", "aleks@weavesocks.com", "aleks", "pass");
        aleks.addCard(new Card("4567456745674567", "10/20", "007"));
        aleks.addAddress(new Address("555", "Spruce St", "Tampa", "33633", "USA"));
        register(aleks);

        User bin = new User("Bin", "Chen", "bin@weavesocks.com", "bin", "pass");
        bin.addCard(new Card("3691369136913691", "01/21", "789"));
        bin.addAddress(new Address("123", "Boston St", "Boston", "01555", "USA"));
        register(bin);

        User harvey = new User("Harvey", "Raja", "harvey@weavesocks.com", "harvey", "pass");
        harvey.addCard(new Card("6854657645765476", "03/22", "456"));
        harvey.addAddress(new Address("123", "O'Farrell St", "San Francisco", "99123", "USA"));
        register(harvey);

        User randy = new User("Randy", "Stafford", "randy@weavesocks.com", "randy", "pass");
        randy.addCard(new Card("6543123465437865", "08/23", "042"));
        randy.addAddress(new Address("123", "Mountain St", "Denver", "74765", "USA"));
        register(randy);
    }
}
