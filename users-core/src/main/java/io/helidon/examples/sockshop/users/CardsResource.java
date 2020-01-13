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

import static io.helidon.examples.sockshop.users.JsonHelpers.embed;
import static io.helidon.examples.sockshop.users.JsonHelpers.obj;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@ApplicationScoped
@Path("/cards")
public class CardsResource {

    @Inject
    private UserRepository users;

    @GET
    @Produces(APPLICATION_JSON)
    public Response getAllCards() {
        return Response.ok(embed("card", Collections.emptyList())).build();
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response registerCard(AddCardRequest req) {
        Card card = new Card(req.longNum, req.expires, req.ccv);
        Card.Id id = users.addCard(req.userID, card);

        return Response.ok(obj().add("id", id.toString()).build()).build();
    }

    @GET
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    public Card getCard(@PathParam("id") Card.Id id) {
        return users.getCard(id).mask();
    }

    @DELETE
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    public Response deleteCard(@PathParam("id") Card.Id id) {
        try {
            users.removeCard(id);
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

    public static class AddCardRequest {
        public String longNum;
        public String expires;
        public String ccv;
        public String userID;
    }
}
