package com.nullteam6.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nullteam6.models.Category;
import com.nullteam6.models.CategoryTemplate;
import com.nullteam6.utility.KitsuCommand;
import com.nullteam6.utility.KitsuUtility;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryService {
    private static List<CategoryTemplate> templateList;

    /**
     * Gets all categories. If the templateList is empty, populates it
     *
     * @return A list of all categories
     * @throws IOException IOException resulting from either a MalformedURLException or a JsonProcessingException
     */
    public List<Category> getAll() throws IOException {
        if (templateList == null)
            templateList = getAllTemplate();
        List<Category> catList = new ArrayList<>();
        for (CategoryTemplate c : templateList) {
            Category cat = new Category(c);
            catList.add(cat);
        }
        return catList;
    }

    /**
     * Get's all the categories of a specific anime
     *
     * @param anime id of the anime
     * @return List of categories associated with the anime
     * @throws IOException IOException resulting from either a MalformedURLException or a JsonProcessingException
     */
    public List<Category> getByAnime(int anime) throws IOException {
        URL url = new URL("https://kitsu.io/api/edge/anime/" + anime + "/categories");
        int count = getCategoryCountByURL(url);
        URL url2 = new URL("https://kitsu.io/api/edge/anime/" + anime + "/categories?page[limit]=" + count);
        List<CategoryTemplate> tList = getAllTemplateByURL(url2);
        List<Category> catList = new ArrayList<>();
        for (CategoryTemplate t : tList) {
            Category c = new Category(t);
            catList.add(c);
        }
        return catList;
    }

    /**
     * Get a list of all the categories directly from Kitsu
     *
     * @return A list of CategoryTemplates
     * @throws IOException IOException resulting from either a JsonProcessingException or MalformedURLException
     */
    private List<CategoryTemplate> getAllTemplate() throws IOException {
        URL url = new URL("https://kitsu.io/api/edge/categories?page[limit]=" + getCategoryCount());
        return getAllTemplateByURL(url);
    }

    /**
     * Gets a list of all categories from a particular URL on kitsu;
     *
     * @param url the url to grab
     * @return returns the list of categorytemplates
     * @throws JsonProcessingException from ObjectMapper
     */
    private List<CategoryTemplate> getAllTemplateByURL(URL url) throws JsonProcessingException {
        KitsuCommand command = new KitsuCommand(null, url);
        KitsuUtility.getInstance().addToQueue(command);
        while (KitsuUtility.getInstance().contains(command)) {
            continue;
        }
        System.out.println(command.getPayload());
        String json = command.getPayload().get("data").toString();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new TypeReference<List<CategoryTemplate>>() {
        });
    }

    /**
     * Get the count of all of the categories from Kitsu
     *
     * @return the number of categories
     * @throws IOException IOException resulting from either a MalformedURLException or JsonProcessingException
     */
    private int getCategoryCount() throws IOException {
        URL url = new URL("https://kitsu.io/api/edge/categories");
        return getCategoryCountByURL(url);
    }

    /**
     * Gets the number of categories associated with a URL
     *
     * @param url the category list URL
     * @return the number of categories
     */
    private int getCategoryCountByURL(URL url) {
        KitsuCommand command = new KitsuCommand(null, url);
        KitsuUtility.getInstance().addToQueue(command);
        while (KitsuUtility.getInstance().contains(command)) {
            continue;
        }
        return command.getPayload().get("meta").get("count").asInt();
    }
}
