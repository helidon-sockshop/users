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

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

public interface AddressApi {
    @GET
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return all addresses associated with a user; or an empty list if no address found")
    @APIResponses({
          @APIResponse(responseCode = "200", description = "if the retrieval is successful")
    })
    Response getAllAddresses();

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Register address for a user; no-op if the address exist")
    @APIResponses({
          @APIResponse(responseCode = "200", description = "if address is successfully registered")
    })
    Response registerAddress(@Parameter(description = "Add Address request") AddressesResource.AddAddressRequest req);

    @GET
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return addresses for the specified identifier")
    Address getAddress(@Parameter(description = "Address identifier")
                       @PathParam("id") AddressId id);

    @DELETE
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Delete address for the specified identifier")
    @APIResponses({
          @APIResponse(responseCode = "200", description = "if address is successfully deleted")
    })
    Response deleteAddress(@Parameter(description = "Address identifier")
                           @PathParam("id") AddressId id);
}
