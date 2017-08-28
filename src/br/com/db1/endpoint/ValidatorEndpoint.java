package br.com.db1.endpoint;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;

import br.com.db1.bol.IPasswordBOL;
import br.com.db1.model.PasswordModel;

/**
 * Endpoint que recebe uma senha e verifica sua força
 * 
 * @author Victor Gil Coronel
 *
 */
@Path("/validator")
public class ValidatorEndpoint {

	@Inject
	private IPasswordBOL passwordBOL;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/validate")
	public Response validate(@QueryParam("password") String password) throws JSONException {

		PasswordModel validatedPassword = passwordBOL.validate(password);

		return Response.ok().entity(validatedPassword).build();
	}
}