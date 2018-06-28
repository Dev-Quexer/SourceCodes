package me.quexer.source.clansystem.manager;

import com.mongodb.ConnectionString;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import org.bson.Document;

import java.text.MessageFormat;

public class MongoManager {

    private final String hostname;
    private final int port;

    private MongoClient client;
    private MongoDatabase database;

    private MongoCollection<Document> clans;

    public MongoManager(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        connect();
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public MongoClient getClient() {
        return client;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public MongoCollection<Document> getClans() {
        return clans;
    }

    public void connect() {
        this.client = MongoClients.create(new ConnectionString(MessageFormat.format("mongodb://{0}:{1}", hostname, String.valueOf(port))));
        this.database = this.client.getDatabase("Source");
        this.clans = this.database.getCollection("clans");


    }


}
