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
import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeService {

    private static final String COUNT = "count";
    private AnimeDAO dao;

    @Autowired
    public void setDao(AnimeDAO dao) {
        this.dao = dao;
    }

    /**
     * Checks the local database for a specific anime by id, if it exists in the database
     * return it, otherwise retrieve it from Kitsu, persist it in the database and return it
     *
     * @param id the id of the anime to look up
     * @return the requested anime
     * @throws IOException IOException either from a MalformedURLException or JsonProcessingException
     */
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

    /**
     * Searches for anime by name
     *
     * @param name The name to search by
     * @return A PaginatedList of the search
     * @throws IOException IOException either from a MalformedURLException or JsonProcessingException
     */
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

    /**
     * Searches for anime with an offset for pagination
     *
     * @param name   Name of the anime to search for
     * @param offset offset for pagination
     * @return PaginatedList of the search
     * @throws IOException IOException from either MalformedURLException or JsonProcessingException
     */
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

    /**
     * Get animes belonging to a certain category
     *
     * @param category the id of the category to search by
     * @return a PaginatedList of the results
     * @throws IOException IOException resulting from either a MalformedURLException or JsonProcessingException
     */
    public PaginatedList<Anime> getByCategory(Integer category) throws IOException {
        URL url = new URL("https://kitsu.io/api/edge/categories/" + category + "/anime");
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
     * Gets animes belonging to a certain category with an offset for pagination
     *
     * @param category the id of the category
     * @param offset   the offset to start at
     * @return a PaginatedList of the search
     * @throws IOException IOException resulting from either a MalformedURLException or a JsonProcessingException
     */
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
     * Get the trending anime list
     *
     * @return A list of trending animes
     * @throws IOException IOException resulting from either a MalformedURLException or JsonProcessingException
     */
    public List<Anime> getTrending() throws IOException {
        URL url = new URL("https://kitsu.io/api/edge/trending/anime");
        KitsuCommand command = new KitsuCommand(null, url);
        KitsuUtility.getInstance().addToQueue(command);
        while (KitsuUtility.getInstance().contains(command)) {
            continue;
        }
        ObjectMapper mapper = new ObjectMapper();
        List<AnimeTemplate> templateList = mapper.readValue(command.getPayload().get("data").toString(), new TypeReference<List<AnimeTemplate>>() {
        });
        List<Anime> animeList = new ArrayList<>();
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
     * @return a PaginatedList of anime
     * @throws IOException  IOException resulting from either a MalformedURLException or a JsonProcessingException
     */
    public PaginatedList<Anime> getAll() throws IOException {
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
     * Gets all anime with an offset!
     * @param offset        offset of animes
     * @return a PaginatedList of all the anime
     * @throws IOException  IOException resulting from either a MalformedURLException or JsonProcessingException
     */
    public PaginatedList<Anime> getAllWithOffset(Integer offset) throws IOException {
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
