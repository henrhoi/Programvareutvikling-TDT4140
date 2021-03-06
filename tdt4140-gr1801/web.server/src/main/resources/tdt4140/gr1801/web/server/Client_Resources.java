package tdt4140.gr1801.web.server;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

//This is one of our API's endpoint "/client", which specifies some GET and POST-requests

@Path("/client")
public class Client_Resources {
	
	
	// GET-request for getting information about a specific client
    @GET
    @Path("/{param}")
    @Produces("application/json")
    public String getClient(@PathParam("param") String clientID) throws NumberFormatException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
    		// Getting PreparedStatement from QueryFactory
    		PreparedStatement stmt = QueryFactory.getClient(Integer.parseInt(clientID));
    		ResultSet rs = stmt.executeQuery();
    		String json = RSJSONConverter.ResultSetToJSON(rs).toString();
    		return json;
	}
    
    
	// GET-request for getting information on all clients of an PT
    @GET
    @Path("/all/{pt}")
    @Produces("application/json")
    public String getClients(@PathParam("pt") String PT_ID) throws NumberFormatException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		// Getting PreparedStatement from QueryFactory
    		PreparedStatement stmt = QueryFactory.getAllClients(PT_ID);
        	ResultSet rs = stmt.executeQuery();
        	String json = RSJSONConverter.ResultSetToJSON(rs).toString();
        	return json;
	}
    
    
    
	// GET-request for all weigth- and fat-data about a specific client
    @GET
    @Path("/weightfat/{clientid}")
    @Produces("application/json")
    public String getWeigthFatFromClient(@PathParam("clientid") String ClientID) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		// Getting PreparedStatement from QueryFactory
    		PreparedStatement stmt = QueryFactory.getWeigthFatFromClient(ClientID);
    		ResultSet rs = stmt.executeQuery();
    		String json = RSJSONConverter.ResultSetToJSON(rs).toString();
    		return json;
    	
    }
   
    
	// GET-request for getting all progressionpictures of a client
    @GET
    @Path("/pics/{clientid}")
    @Produces
    public String getClientProgressionPictures(@PathParam("clientid") String ClientID) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		// Getting PreparedStatement from QueryFactory
    		PreparedStatement stmt = QueryFactory.getClientProgressionPictures(ClientID);
    		ResultSet rs = stmt.executeQuery();
    		String json = RSJSONConverter.ResultSetToJSON(rs).toString();
    		return json;
    		
    }
    
    
	// POST-request for removing a client from the database along with all connected data
    @POST
    @Path("/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    public static Response deleteClient(String data) throws NoSuchAlgorithmException, ClientProtocolException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		JSONObject json = new JSONObject(data);
		String username = json.getString("PT_ID");
		String password = json.getString("Passwrd");
		int clientID = json.getInt("ClientID");
		
		// Getting PreparedStatement from QueryFactory
		PreparedStatement stmt = QueryFactory.getPTforClient(clientID);
		ResultSet res = stmt.executeQuery();
		String pt_id = null;
		while (res.next()) {
			pt_id = res.getString("PT_ID");
		}
		
		// Authenticating the PT that is trying to delete that specific client.
		// "Is the PT the personal trainer to client with this specific ClientID"
		 if (username.equals(pt_id)) {
			 //Checking if the input password is corresponding with the password in database
			if(LoginModule.checkLogin(username, password)) {
				// Retrieving all statements necessary for deleting all current informaiton about an Client.
				List<PreparedStatement> statements = QueryFactory.deleteAllClientData(clientID);
				List<String> beingDeleted = Arrays.asList("Nutrition", "Endurance","Exercise", "Strength", "ProgressionPictures", "WeeklyProgram", "ClientWeight", "Client");
				for (int i=0; i<statements.size();i++) {
					// Executing all PreparedStatements
					statements.get(i).execute();
					System.out.println("Deleting " + beingDeleted.get(i));
				}
				//If everything went ok a "201 CREATED" response will be sent with a corresponding message
				return Response.status(201).entity(username + " with data was deleted in database if all input were correct").build(); 
    		} else {
    			//If the input was wrong a "400 BAD REQUEST" response will be sent
    			return Response.status(400).entity("Wrong username or password. Please try again.").build(); 
    			}
		} else {
			//If the input was wrong a "400 BAD REQUEST" response will be sent
			return Response.status(400).entity("This client does not belong to "+username).build(); 
		}
    }
    
}
