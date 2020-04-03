package com.nullteam6.service;

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

    private List<CategoryTemplate> getAllTemplate() throws IOException {
        URL url = new URL("https://kitsu.io/api/edge/categories?page[limit]=" + getCategoryCount());
        KitsuCommand command = new KitsuCommand(null, url);
        KitsuUtility.getInstance().addToQueue(command);
        while (KitsuUtility.getInstance().contains(command)) {
            continue;
        }
        String json = command.getPayload().get("data").toString();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new TypeReference<List<CategoryTemplate>>() {
        });
    }

    private int getCategoryCount() throws IOException {
        URL url = new URL("https://kitsu.io/api/edge/categories");
        KitsuCommand command = new KitsuCommand(null, url);
        KitsuUtility.getInstance().addToQueue(command);
        while (KitsuUtility.getInstance().contains(command)) {
            continue;
        }
        return command.getPayload().get("meta").get("count").asInt();
    }
}
