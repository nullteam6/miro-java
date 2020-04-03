package com.nullteam6.utility;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class KitsuUtility {
    private static KitsuUtility instance = null;
    Thread dt;
    private BlockingQueue<KitsuCommand> queue;

    private KitsuUtility() {
        super();
        this.queue = new ArrayBlockingQueue<>(1024);
        initThread();
    }

    public static KitsuUtility getInstance() {
        if (instance == null) {
            instance = new KitsuUtility();
        }
        return instance;
    }

    public void addToQueue(KitsuCommand command) {
        queue.add(command);
        if (!dt.isAlive()) {
            initThread();
        }
    }

    private void initThread() {
        dt = new Thread(new KitsuDaemon(queue), "dt");
        dt.setDaemon(true);
        dt.start();
    }

    public boolean contains(KitsuCommand cmd) {
        return queue.contains(cmd);
    }


}

class KitsuDaemon implements Runnable {
    private static final int RATE_LIMIT = 333;
    private BlockingQueue<KitsuCommand> queue;

    public KitsuDaemon(BlockingQueue<KitsuCommand> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (!queue.isEmpty()) {
            try {
                KitsuCommand command = queue.remove();
                synchronized (command) {
                    System.out.println("Daemon executing task");
                    executeCommand(command);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                Thread.sleep(RATE_LIMIT);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

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