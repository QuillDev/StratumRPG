package moe.quill.stratumrpg.Database;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import moe.quill.stratumrpg.Config.ConfigManager;
import moe.quill.stratumrpg.Players.StratumPlayer;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseManager implements Listener {

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> playerCollection;
    private final ConfigManager configManager;

    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class.getSimpleName());

    public DatabaseManager(ConfigManager configManager) {
        this.configManager = configManager;

        this.mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .uuidRepresentation(UuidRepresentation.STANDARD)
                        .build());

        this.database = mongoClient.getDatabase(configManager.getDatabaseName());
        this.playerCollection = this.database.getCollection("players");
    }


    public StratumPlayer getPlayer(Player player) {
        final var playerDocument = findPlayer(player);

        if (playerDocument == null) {
            logger.error("Could not find the given player! Attempting to create a new entry!");
            final var newPlayerDocument = createNewPlayer(player);

            //if the new player document is not empty, return a new stratum player
            if (!newPlayerDocument.isEmpty()) {
                return new StratumPlayer(player, newPlayerDocument);
            }

            //If both attempts were bad, just return null
            return null;
        }

        return new StratumPlayer(player, playerDocument);
    }

    /**
     * Find the players database information from the given uuid
     *
     * @param player to find a matching document for
     * @return the first matching document
     */
    public Document findPlayer(Player player) {
        return playerCollection.find(Filters.eq("uuid", player.getUniqueId())).first();
    }

    public void updatePlayerDocument(StratumPlayer stratumPlayer) {

        //Try to update the player
        playerCollection.findOneAndUpdate(
                Filters.eq("uuid", stratumPlayer.getPlayer().getUniqueId()),
                stratumPlayer.getUpdateBSON()
        );
    }

    /**
     * Create a new database entry from the given player
     *
     * @param player to create a new database entry for
     */
    public Document createNewPlayer(Player player) {

        final var existingPlayer = findPlayer(player);
        if (existingPlayer != null) {
            logger.error(String.format("Tried to add player that already exists! Name: %s | UUID: %s", player.getName(), player.getUniqueId()));
            return new Document();
        }

        // Create the player document here
        final var user = new Document()
                //Base Information
                .append("username_display", player.getName())
                .append("username_lower", player.getName().toLowerCase())
                .append("uuid", player.getUniqueId());

        //try to intert the player into the collection
        final var result = playerCollection.insertOne(user);

        //If it was acknowledged return the player document we inserted
        if (result.wasAcknowledged()) {
            logger.info(String.format("User %s | UUID: %s was added to the database.", player.getName(), player.getUniqueId()));
            return user;
        }

        logger.error(String.format("Failed to add user %s | UUID: %s to the database!", player.getName(), player.getUniqueId()));
        return new Document();
    }
}
