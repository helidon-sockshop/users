package io.helidon.examples.sockshop.users.jpa;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import io.helidon.examples.sockshop.users.Address;
import io.helidon.examples.sockshop.users.Card;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import io.helidon.examples.sockshop.users.DefaultUserRepository;
import io.helidon.examples.sockshop.users.User;

import org.eclipse.microprofile.opentracing.Traced;

/**
 * An implementation of {@link io.helidon.examples.sockshop.users.UserRepository}
 * that that uses relational database (via JPA) as a backend data store.
 */
@ApplicationScoped
@Specializes
@Traced
public class JpaUserRepository extends DefaultUserRepository {

    @PersistenceContext
    private EntityManager em;

    private volatile boolean fLoaded = false;

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public Address getAddress(Address.Id id) {
        return em.find(User.class, id.getCustomerId()).getAddress(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public Address.Id addAddress(String userID, Address address) {
        User u = em.find(User.class, userID);
        Address.Id id = u.addAddress(address).getId();

        em.persist(u);
        return id;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void removeAddress(Address.Id id) {
        User u = em.find(User.class, id.getCustomerId());
        u.removeAddress(id);

        em.persist(u);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public Card.Id addCard(String userID, Card card) {
        User u = em.find(User.class, userID);
        Card.Id id = u.addCard(card).getId();

        em.persist(u);
        return id;
    }

    @Override
    @Transactional
    public Card getCard(Card.Id id) {
        return em.find(User.class, id.getCustomerId()).getCard(id);
    }

    @Override
    @Transactional
    public void removeCard(Card.Id id) {
        User u = em.find(User.class, id.getCustomerId());
        u.removeCard(id);
        em.persist(u);
    }

    @Override
    @Transactional
    public User getUser(String userId) {
        return em.find(User.class, userId);
    }

    @Override
    @Transactional
    public Collection<? extends User> getAllUsers() {
        return super.getAllUsers();
    }

    @Override
    public User getOrCreate(String userId) {
        User u = em.find(User.class, userId);
        if (u == null) {
            u = new User();
            u.setUsername(userId);
            em.persist(u);
        }
        return u;
    }

    @Override
    @Transactional
    public User removeUser(String userId) {
        User u = em.find(User.class, userId);
        if (u != null) {
            em.remove(u);
        }
        return u;
    }

    @Override
    @Transactional
    public boolean authenticate(String username, String password) {
        User user = em.find(User.class, username);
        return user != null ? user.authenticate(password) : false;
    }

    @Override
    @Transactional
    public User register(User user) {
        User u = em.find(User.class, user.getUsername());
        if (u == null) {
            em.persist(user);
            u = user;
        }
        return u;
    }
}
