/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.users.coherence;

import com.tangosol.net.CacheFactory;

import io.helidon.examples.sockshop.users.UserRepository;
import io.helidon.examples.sockshop.users.UserRepositoryTest;

/**
 * Tests for Coherence repository implementation.
 */
class CoherenceUserRepositoryIT extends UserRepositoryTest {

    public UserRepository getUserRepository() {
        return new CoherenceUserRepository(CacheFactory.getCache("users"));
    }
}