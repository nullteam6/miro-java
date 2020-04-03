package com.nullteam6.controller;

import com.nullteam6.models.Category;
import com.nullteam6.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService service;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List<Category> listAll() throws Exception {
        return service.getAll();
    }
}
