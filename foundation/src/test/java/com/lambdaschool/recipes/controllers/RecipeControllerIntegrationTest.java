package com.lambdaschool.recipes.controllers;

import com.lambdaschool.recipes.RecipesApplication;
import com.lambdaschool.recipes.models.RecipeMinimal;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RecipesApplication.class)
@AutoConfigureMockMvc
@WithUserDetails(value = "junior")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RecipeControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws
            Exception
    {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void a_listAuthedRecipes() throws Exception {
        mockMvc.perform(get("/recipes/all"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Junior's fruit cocktail")));
    }

    @Test
    public void b_getRecipeById() throws Exception {
        mockMvc.perform(get("/recipes/13"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Junior's fruit cocktail")));
    }

    @Test
    public void c_getRecipeByIdNotFound() throws Exception {
        mockMvc.perform(get("/recipes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void d_postNewRecipe() throws Exception {
        RecipeMinimal rm = new RecipeMinimal();
        rm.setName("Grandma's butter cookies");
        rm.setSource("Grandma");
        rm.setCategory("dessert");
        rm.setInstructions("some instructions");
        rm.getIngredients().add("butter");
        rm.getIngredients().add("cookies");
        rm.getGuests().add("mother");

        mockMvc.perform(post("/recipes/new")
                .content(new ObjectMapper().writeValueAsString(rm))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Grandma's butter cookies")));
    }

    @Test
    public void e_putRecipe() throws Exception {
        RecipeMinimal rm = new RecipeMinimal();
        rm.setName("Grandma's butter cookies");
        rm.setSource("Grandma");
        rm.setCategory("dessert");
        rm.setInstructions("some instructions");
        rm.getIngredients().add("butter");
        rm.getIngredients().add("cookies");
        rm.getGuests().add("mother");

        mockMvc.perform(put("/recipes/13")
                .content(new ObjectMapper().writeValueAsString(rm))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Grandma's butter cookies")));
    }

    @Test
    public void f_patchRecipe() throws Exception {
        RecipeMinimal rm = new RecipeMinimal();
        rm.setName("Extra spicy nachos");
        rm.setSource("Jose");
        rm.setCategory("snacks");
        rm.setInstructions("some instructions");
        rm.getIngredients().add("spice");
        rm.getIngredients().add("nachos");
        rm.getIngredients().add("extra");
        //rm.getGuests().add("mother");

        mockMvc.perform(patch("/recipes/13")
                .content(new ObjectMapper().writeValueAsString(rm))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Extra spicy nachos")));
    }

    @Test
    public void g_deleteRecipe() throws Exception {
        mockMvc.perform(delete("/recipes/13"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("Junior's fruit cocktail"))));
    }
}