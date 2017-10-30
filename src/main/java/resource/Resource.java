/**
 * 
 */
package resource;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import app.data.GamesStorage;
import game.Game;
import game.SimpleMove;
import game.tictactoe.TicTacToeGame;

/**
 * @author yakov
 *
 */
@Path("/tictaktoe")
public class Resource {

    @GET
    @Path("/new")
    @Produces(MediaType.APPLICATION_JSON)
    public Game newGame(@QueryParam("vsAI") Boolean vsAI, @QueryParam("difficulty") Integer difficulty) {
        Game game = new TicTacToeGame(UUID.randomUUID().toString(), vsAI, difficulty);
        GamesStorage.games.put(game.id, game);
        return game;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Game get(@PathParam("id") String id) {
        return GamesStorage.games.get(id);
    }

    @POST
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Game post(@PathParam("id") String id, SimpleMove move) {
        Game game = GamesStorage.games.get(id);
        game.makeMove(move);
        return game;
    }

}
