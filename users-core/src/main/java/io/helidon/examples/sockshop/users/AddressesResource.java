package io.helidon.examples.sockshop.users;

import java.util.Collections;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import static io.helidon.examples.sockshop.users.JsonHelpers.embed;
import static io.helidon.examples.sockshop.users.JsonHelpers.obj;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@ApplicationScoped
@Path("/addresses")
public class AddressesResource {

    @Inject
    private UserRepository users;

    @GET
    @Produces(APPLICATION_JSON)
    public Response getAllAddresses() {
        return Response.ok(embed("address", Collections.emptyList())).build();
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response registerAddress(AddAddressRequest req) {
        Address address = new Address(req.number, req.street, req.city, req.postcode, req.country);
        Address.Id id = users.addAddress(req.userID, address);

        return Response.ok(obj().add("id", id.toString()).build()).build();
    }

    @GET
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    public Address getAddress(@PathParam("id") Address.Id id) {
        return users.getAddress(id);
    }

    @DELETE
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    public Response deleteAddress(@PathParam("id") Address.Id id) {
        try {
            users.removeAddress(id);
            return status(true);
        }
        catch (RuntimeException e) {
            return status(false);
        }
    }

    // --- helpers ----------------------------------------------------------

    private static Response status(boolean fSuccess) {
        return Response.ok(obj().add("status", fSuccess).build()).build();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddAddressRequest {
        public String number;
        public String street;
        public String city;
        public String postcode;
        public String country;
        public String userID;
    }
}
