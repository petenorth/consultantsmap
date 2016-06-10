/**
 *  Copyright 2005-2015 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package com.redhat.ukihackathon;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.annotations.ApiResponse;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Java class with be hosted in the URI path defined by the @Path annotation. @Path annotations on the methods
 * of this class always refer to a path relative to the path defined at the class level.
 * <p/>
 * For example, with 'http://localhost:8181/cxf' as the default CXF servlet path and '/crm' as the JAX-RS server path,
 * this class will be hosted in 'http://localhost:8181/cxf/crm/customerservice'.  An @Path("/customers") annotation on
 * one of the methods would result in 'http://localhost:8181/cxf/crm/customerservice/customers'.
 */
@Path("/consultantlocations/")
@Api(value = "/consultantlocations", description = "Operations about consultant locations")
public class CustomerService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerService.class);

    private ConsultantLocations consultantLocations = new ConsultantLocations();
    private MessageContext jaxrsContext;


    public CustomerService() {
        init();
    }

    /**
     * This method is mapped to an HTTP GET of 'http://localhost:8181/cxf/crm/customerservice/customers/{id}'.  The value for
     * {id} will be passed to this message as a parameter, using the @PathParam annotation.
     * <p/>
     * The method returns a Customer object - for creating the HTTP response, this object is marshaled into XML using JAXB.
     * <p/>
     * For example: surfing to 'http://localhost:8181/cxf/crm/customerservice/customers/123' will show you the information of
     * customer 123 in XML format.
     */
    @GET
    @Path("/locations/{id}/")
    @Produces("application/json")
    @ApiOperation(value = "Find Customer by ID", notes = "More notes about this method", response = Customer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Invalid ID supplied"),
            @ApiResponse(code = 204, message = "location not found")
    })
    public ConsultantLocations getConsultantLocations(@ApiParam(value = "ID of customerlocation to fetch", required = true) @PathParam("id") String id) {
        LOG.info("Invoking getCustomer, Customer id is: {}", id);
        return consultantLocations;
    }

    /**
     * Using HTTP PUT, we can can upload the XML representation of a customer object.  This operation will be mapped
     * to the method below and the XML representation will get unmarshaled into a real Customer object using JAXB.
     * <p/>
     * The method itself just updates the customer object in our local data map and afterwards uses the Reponse class to
     * build the appropriate HTTP response: either OK if the update succeeded (translates to HTTP Status 200/OK) or not
     * modified if the method failed to update a customer object (translates to HTTP Status 304/Not Modified).
     * <p/>
     * Note how this method is using the same @Path value as our next method - the HTTP method used will determine which
     * method is being invoked.
     */


    /**
     * Using HTTP POST, we can add a new customer to the system by uploading the XML representation for the customer.
     * This operation will be mapped to the method below and the XML representation will get unmarshaled into a real
     * Customer object.
     * <p/>
     * After the method has added the customer to the local data map, it will use the Response class to build the HTTP reponse,
     * sending back the inserted customer object together with a HTTP Status 200/OK.  This allows us to send back the
     * new id for the customer object to the client application along with any other data that might have been updated in
     * the process.
     * <p/>
     * Note how this method is using the same @Path value as our previous method - the HTTP method used will determine which
     * method is being invoked.
     */
    @POST
    @Path("/consultantlocations/")
    @Consumes({"application/json"})
    @ApiOperation(value = "Add a new consultant locations")
    @ApiResponses(value = {@ApiResponse(code = 500, message = "Invalid ID supplied"),})
    public Response addConsultantLocations(@ApiParam(value = "Customer object that needs to be updated", required = true)
                                ConsultantLocations consultantLocations) {
        this.consultantLocations = consultantLocations;
        if (jaxrsContext.getHttpHeaders().getMediaType().getSubtype().equals("json")) {
            return Response.ok().type("application/json").entity(consultantLocations).build();
        } else {
            return Response.ok().type("application/xml").entity(consultantLocations).build();
        }
    }





    /**
     * The init method is used by the constructor to insert a Customer and Order object into the local data map
     * for testing purposes.
     */
    final void init() {
    	ConsultantLocation consultantLocation1 = new ConsultantLocation();
    	consultantLocation1.setName("Matt Roberts");
    	consultantLocation1.setLocation("SW1A 2NH");
    	ConsultantLocation consultantLocation2 = new ConsultantLocation();
    	consultantLocation2.setName("Paulo Menon");
    	consultantLocation2.setLocation("EC2M 7LS");
    	consultantLocations.getLocations().add(consultantLocation1);
    	consultantLocations.getLocations().add(consultantLocation2);

    }

    @Context
    public void setMessageContext(MessageContext messageContext) {
        this.jaxrsContext = messageContext;
    }


}
