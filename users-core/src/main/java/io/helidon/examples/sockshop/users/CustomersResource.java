package io.helidon.examples.sockshop.users;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static io.helidon.examples.sockshop.users.JsonHelpers.embed;
import static io.helidon.examples.sockshop.users.JsonHelpers.obj;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@ApplicationScoped
@Path("/customers")
public class CustomersResource {

    @Inject
    private UserRepository users;

    @GET
    @Produces(APPLICATION_JSON)
    public Response getAllCustomers() {
        return Response.ok(embed("customer", users.getAllUsers())).build();
    }

    @GET
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    public Response getCustomer(@PathParam("id") String id) {
        return Response.ok(users.getOrCreate(id)).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    public Response deleteCustomer(@PathParam("id") String id) {
        User prev = users.removeUser(id);
        return Response.ok(obj().add("status", prev != null).build()).build();
    }

    @GET
    @Path("{id}/cards")
    @Produces(APPLICATION_JSON)
    public Response getCustomerCards(@PathParam("id") String id) {
        User user = users.getUser(id);
        return Response.ok(embed("card", user.getCards().stream().map(Card::mask).toArray())).build();
    }

    @GET
    @Path("{id}/addresses")
    @Produces(APPLICATION_JSON)
    public Response getCustomerAddresses(@PathParam("id") String id) {
        User user = users.getUser(id);
        return Response.ok(embed("address", user.getAddresses())).build();
    }
}
