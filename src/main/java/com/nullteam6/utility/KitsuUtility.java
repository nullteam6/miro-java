package com.nullteam6.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class KitsuUtility {
    private static KitsuUtility instance = null;
    Thread dt;
    private final BlockingQueue<KitsuCommand> queue;

    private KitsuUtility() {
        super();
        this.queue = new ArrayBlockingQueue<>(1024);
        initThread();
    }

    /**
     * Gets the instance of KitsuUtility for executing commands
     *
     * @return the instance of KitsuUtility
     */
    public static KitsuUtility getInstance() {
        if (instance == null) {
            instance = new KitsuUtility();
        }
        return instance;
    }

    /**
     * Add a command to queue
     *
     * @param command a KitsuCommand containing the URL to retrieve
     */
    public void addToQueue(KitsuCommand command) {
        queue.add(command);
        if (!dt.isAlive()) {
            initThread();
        }
    }

    /**
     * initiates a new scheduler if the old one has ended due to inactivity
     */
    private void initThread() {
        dt = new Thread(new KitsuDaemon(queue), "dt");
        dt.setDaemon(true);
        dt.start();
    }

    /**
     * check to see if the KitsuCommand queue contains an object
     *
     * @param cmd the KitsuCommand to check
     * @return true if the command is still in queue or false otherwise
     */
    public boolean contains(KitsuCommand cmd) {
        return queue.contains(cmd);
    }


}

class KitsuDaemon implements Runnable {
    private static final int RATE_LIMIT = 300;
    private final BlockingQueue<KitsuCommand> queue;
    protected Logger logger = LogManager.getLogger();

    public KitsuDaemon(BlockingQueue<KitsuCommand> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (!queue.isEmpty()) {
            try {
                KitsuCommand command = queue.remove();
                synchronized (command) {
                    executeCommand(command);
                }
            } catch (IOException ex) {
                logger.debug(ex.getMessage());
            }
            try {
                Thread.sleep(RATE_LIMIT);
            } catch (InterruptedException ex) {
                logger.debug(ex.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Retrieves JSON from Kitsu
     *
     * @param command the KitsuCommand containing the URL to retrieve
     * @throws IOException IOException resulting from either a MalformedURLException or a JsonProcessingException
     */
    private void executeCommand(KitsuCommand command) throws IOException {
        HttpURLConnection con = (HttpURLConnection) command.getUrl().openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/vnd.api+json");
        con.setRequestProperty("ContentType", "application/vnd.api+json");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        ObjectMapper mapper = new ObjectMapper();
        command.setPayload(mapper.readTree(content.toString()));
        command.setCompleted(true);
    }
}