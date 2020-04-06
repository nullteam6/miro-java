package com.nullteam6.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nullteam6.models.Anime;
import com.nullteam6.models.AnimeTemplate;
import com.nullteam6.utility.KitsuCommand;
import com.nullteam6.utility.KitsuUtility;
import com.nullteam6.utility.PaginatedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Component
public class AnimeService {

    private static final String COUNT = "count";
    private AnimeDAO dao;

    @Autowired
    public void setDao(AnimeDAO dao) {
        this.dao = dao;
    }

    public Anime getById(int id) throws IOException {
        Anime a;
        a = dao.get(id);
        if (a == null) {
            URL url = new URL("https://kitsu.io/api/edge/anime/" + id);
            KitsuCommand command = new KitsuCommand(null, url);
            KitsuUtility.getInstance().addToQueue(command);
            while (KitsuUtility.getInstance().contains(command)) {
                continue;
            }
            ObjectMapper mapper = new ObjectMapper();
            AnimeTemplate template = mapper.readValue(command.getPayload().get("data").toString(), AnimeTemplate.class);
            a = new Anime(template);
            dao.add(a);
        }
        return a;
    }

    public PaginatedList<Anime> searchForAnime(String name) throws IOException {
        URL url = new URL("https://kitsu.io/api/edge/anime?filter[text]=" + name.replace(" ", "%20"));
        KitsuCommand command = new KitsuCommand(null, url);
        KitsuUtility.getInstance().addToQueue(command);
        while (KitsuUtility.getInstance().contains(command)) {
            continue;
        }
        ObjectMapper mapper = new ObjectMapper();
        List<AnimeTemplate> templateList = mapper.readValue(command.getPayload().get("data").toString(), new TypeReference<List<AnimeTemplate>>() {
        });
        PaginatedList<Anime> aniList = new PaginatedList<>();
        aniList.setTotalCount(command.getPayload().get("meta").get(COUNT).intValue());
        aniList.setNext("10");
        aniList.setLast(String.valueOf(aniList.getTotalCount() - 10));
        for (AnimeTemplate t : templateList) {
            Anime a = new Anime(t);
            aniList.add(a);
        }
        return aniList;
    }

    public PaginatedList<Anime> searchForOffset(String name, int offset) throws IOException {
        URL url = new URL("https://kitsu.io/api/edge/anime?filter[text]=" + name.replace(" ", "%20") + "&page[offset]=" + offset);
        KitsuCommand command = new KitsuCommand(null, url);
        KitsuUtility.getInstance().addToQueue(command);
        while (KitsuUtility.getInstance().contains(command)) {
            continue;
        }
        ObjectMapper mapper = new ObjectMapper();
        List<AnimeTemplate> templateList = mapper.readValue(command.getPayload().get("data").toString(), new TypeReference<List<AnimeTemplate>>() {
        });
        PaginatedList<Anime> aniList = new PaginatedList<>();
        aniList.setNext(String.valueOf(offset + 10));
        aniList.setTotalCount(command.getPayload().get("meta").get(COUNT).intValue());
        for (AnimeTemplate t : templateList) {
            Anime a = new Anime(t);
            aniList.add(a);
        }
        return aniList;
    }

    public PaginatedList<Anime> getByCategory(Integer category) throws IOException {
        URL url = new URL("https://kitsu.io/api/edge/category/" + category + "/anime");
        KitsuCommand command = new KitsuCommand(null, url);
        KitsuUtility.getInstance().addToQueue(command);
        while (KitsuUtility.getInstance().contains(command)) {
            continue;
        }
        ObjectMapper mapper = new ObjectMapper();
        List<AnimeTemplate> templateList = mapper.readValue(command.getPayload().get("data").toString(), new TypeReference<List<AnimeTemplate>>() {
        });
        PaginatedList<Anime> animeList = new PaginatedList<>();
        animeList.setNext("10");
        animeList.setTotalCount(command.getPayload().get("meta").get(COUNT).intValue());
        animeList.setLast(String.valueOf(animeList.getTotalCount() - 10));
        for (AnimeTemplate t : templateList) {
            Anime a = new Anime(t);
            animeList.add(a);
        }
        return animeList;
    }

    public PaginatedList<Anime> getByCategoryOffset(Integer category, Integer offset) throws IOException {
        URL url = new URL("https://kitsu.io/api/edge/category/" + category + "/anime?page[offset]=" + offset);
        KitsuCommand command = new KitsuCommand(null, url);
        KitsuUtility.getInstance().addToQueue(command);
        while (KitsuUtility.getInstance().contains(command)) {
            continue;
        }
        ObjectMapper mapper = new ObjectMapper();
        List<AnimeTemplate> templateList = mapper.readValue(command.getPayload().get("data").toString(), new TypeReference<List<AnimeTemplate>>() {
        });
        PaginatedList<Anime> animeList = new PaginatedList<>();
        animeList.setNext(String.valueOf(offset + 10));
        animeList.setTotalCount(command.getPayload().get("meta").get(COUNT).intValue());
        animeList.setLast(String.valueOf(animeList.getTotalCount() - 10));
        for (AnimeTemplate t : templateList) {
            Anime a = new Anime(t);
            animeList.add(a);
        }
        return animeList;
    }

    /**
     * Returns the beginning page of the entire Kitsu anime collection.
     * <b>Use with great caution</b>
     *
     * @return The start of the end of your life
     * @throws Exception Possibility of yeeting a JsonProcessingException and a MalformedURLException
     */
    public PaginatedList<Anime> jeanCena() throws IOException {
        URL url = new URL("https://kitsu.io/api/edge/anime");
        KitsuCommand command = new KitsuCommand(null, url);
        KitsuUtility.getInstance().addToQueue(command);
        while (KitsuUtility.getInstance().contains(command)) {
            continue;
        }
        ObjectMapper mapper = new ObjectMapper();
        List<AnimeTemplate> templateList = mapper.readValue(command.getPayload().get("data").toString(), new TypeReference<List<AnimeTemplate>>() {
        });
        PaginatedList<Anime> animeList = new PaginatedList<>();
        animeList.setNext("10");
        animeList.setTotalCount(command.getPayload().get("meta").get(COUNT).intValue());
        animeList.setLast(String.valueOf(animeList.getTotalCount() - 10));
        for (AnimeTemplate t : templateList) {
            Anime a = new Anime(t);
            animeList.add(a);
        }
        return animeList;
    }

    /**
     * You didn't read the JavaDocs for this class and
     * now you're back for more?
     *
     * @param offset the starting entry for the page to be constructed
     * @return Cheetos and lots of them
     * @throws Exception Possibility of yeeting an IOException
     */
    public PaginatedList<Anime> downTheRabbitHole(Integer offset) throws IOException {
        URL url = new URL("https://kitso.io/api/edge/anime?page[offset]=" + offset);
        KitsuCommand command = new KitsuCommand(null, url);
        KitsuUtility.getInstance().addToQueue(command);
        while (KitsuUtility.getInstance().contains(command)) {
            continue;
        }
        ObjectMapper mapper = new ObjectMapper();
        List<AnimeTemplate> templateList = mapper.readValue(command.getPayload().get("data").toString(), new TypeReference<List<AnimeTemplate>>() {
        });
        PaginatedList<Anime> animeList = new PaginatedList<>();
        animeList.setNext(String.valueOf(offset + 10));
        animeList.setTotalCount(command.getPayload().get("meta").get(COUNT).intValue());
        animeList.setLast(String.valueOf(animeList.getTotalCount() - 10));
        for (AnimeTemplate t : templateList) {
            Anime a = new Anime(t);
            animeList.add(a);
        }
        return animeList;
    }
}
