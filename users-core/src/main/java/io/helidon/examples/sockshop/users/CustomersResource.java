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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import static io.helidon.examples.sockshop.users.JsonHelpers.embed;
import static io.helidon.examples.sockshop.users.JsonHelpers.obj;

@ApplicationScoped
@Path("/customers")
public class CustomersResource implements CustomerApi {

    @Inject
    private UserRepository users;

    @Override
    public Response getAllCustomers() {
        return Response.ok(embed("customer", users.getAllUsers())).build();
    }

    @Override
    public Response getCustomer(String id) {
        return Response.ok(users.getOrCreate(id)).build();
    }

    @Override
    public Response deleteCustomer(String id) {
        User prev = users.removeUser(id);
        return Response.ok(obj().add("status", prev != null).build()).build();
    }

    @Override
    public Response getCustomerCards(String id) {
        User user = users.getUser(id);
        return Response.ok(embed("card", user.getCards().stream().map(Card::mask).toArray())).build();
    }

    @Override
    public Response getCustomerAddresses(String id) {
        User user = users.getUser(id);
        return Response.ok(embed("address", user.getAddresses())).build();
    }
}
