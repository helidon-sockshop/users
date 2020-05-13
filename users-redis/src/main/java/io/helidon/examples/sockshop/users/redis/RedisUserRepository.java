package io.helidon.examples.sockshop.users.redis;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import io.helidon.examples.sockshop.users.DefaultUserRepository;
import io.helidon.examples.sockshop.users.User;

import org.eclipse.microprofile.opentracing.Traced;
import org.redisson.api.RMap;

/**
 * An implementation of {@link io.helidon.examples.sockshop.users.UserRepository}
 * that that uses Redis (via Redisson) as a backend data store.
 */
@ApplicationScoped
@Specializes
@Traced
public class RedisUserRepository extends DefaultUserRepository {
    @Inject
    public RedisUserRepository(RMap<String, User> users) {
        super(users);
    }
}