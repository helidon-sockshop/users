package io.helidon.examples.sockshop.users.jpa;

import java.util.Collection;

import io.helidon.examples.sockshop.users.Address;
import io.helidon.examples.sockshop.users.AddressId;
import io.helidon.examples.sockshop.users.Card;
import io.helidon.examples.sockshop.users.CardId;
import io.helidon.examples.sockshop.users.UserRepository;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import io.helidon.examples.sockshop.users.User;

import org.eclipse.microprofile.opentracing.Traced;

import static javax.interceptor.Interceptor.Priority.APPLICATION;

/**
 * An implementation of {@link io.helidon.examples.sockshop.users.UserRepository}
 * that that uses relational database (via JPA) as a backend data store.
 */
@ApplicationScoped
@Alternative
@Priority(APPLICATION)
@Traced
public class JpaUserRepository implements UserRepository
    {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Address getAddress(AddressId id) {
        return em.find(User.class, id.getUser()).getAddress(id.getAddressId());
    }

    @Override
    @Transactional
    public AddressId addAddress(String userID, Address address) {
        User u = em.find(User.class, userID);
        AddressId id = u.addAddress(address).getId();

        em.persist(u);
        em.persist(address);
        return id;
    }

    @Override
    @Transactional
    public void removeAddress(AddressId id) {
        User u = em.find(User.class, id.getUser());
        u.removeAddress(id.getAddressId());

        em.persist(u);
    }

    @Override
    @Transactional
    public CardId addCard(String userID, Card card) {
        User u = em.find(User.class, userID);
        CardId id = u.addCard(card).getId();

        em.persist(u);
        em.persist(card);
        return id;
    }

    @Override
    @Transactional
    public Card getCard(CardId id) {
        return em.find(User.class, id.getUser()).getCard(id.getCardId());
    }

    @Override
    @Transactional
    public void removeCard(CardId id) {
        User u = em.find(User.class, id.getUser());
        u.removeCard(id.getCardId());
        em.persist(u);
    }

    @Override
    @Transactional
    public User getUser(String userId) {
        return em.find(User.class, userId);
    }

    @Override
    @Transactional
    public User getOrCreate(String userId) {
        User u = em.find(User.class, userId);
        if (u == null) {
            u = new User(userId);
        }
        return u;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public User removeUser(String userId) {
        User u = em.find(User.class, userId);
        if (u != null) {
            em.remove(u);
        }
        return u;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public boolean authenticate(String username, String password) {
        User user = em.find(User.class, username);
        return user != null ? user.authenticate(password) : false;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public User register(User user) {
        User u = em.find(User.class, user.getUsername());
        if (u == null) {
            em.persist(user);
        }

        return u;
    }

    @Override
    @Transactional
    public Collection<? extends User> getAllUsers() {

        StringBuilder jql = new StringBuilder("select u from User as u");

        TypedQuery query = em.createQuery(jql.toString(), User.class);

        return query.getResultList();
    }
}
