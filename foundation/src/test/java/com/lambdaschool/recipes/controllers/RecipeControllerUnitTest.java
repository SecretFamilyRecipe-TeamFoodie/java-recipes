package com.lambdaschool.recipes.controllers;

import com.lambdaschool.recipes.RecipesApplication;
import com.lambdaschool.recipes.models.*;
import com.lambdaschool.recipes.repository.UserRepository;
import com.lambdaschool.recipes.services.RecipeService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RecipesApplication.class)
@AutoConfigureMockMvc
@WithMockUser(username = "owner",
        roles = {"USER", "ADMIN"})
public class RecipeControllerUnitTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @MockBean
    private UserRepository userRepository;

    private List<Recipe> recipes = new ArrayList<>();

    private List<RecipeMinimal> minimals = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        User owner = new User("owner", "ownerpass", "owneremail");
        owner.setUserid(1);

        User guest = new User("guest", "guestpass", "guestemail");
        guest.setUserid(2);

        Recipe r1 = new Recipe("spicy chicken soup", "John", "do stuff","soup");
        r1.setRecipeid(1);
        Recipe r2 = new Recipe("that thing your aunt made you don't know what it is", "John's old aunt", "do stuff","none");
        r2.setRecipeid(2);
        Recipe r3 = new Recipe("grandma's kettle corn", "Grandma", "do stuff","snacks");
        r3.setRecipeid(3);

        r1.setOwner(owner);
        r2.setOwner(owner);
        r3.setOwner(owner);

        r1.getGuests().add(new UserRecipe(guest, r1));
        r2.getGuests().add(new UserRecipe(guest, r2));
        r3.getGuests().add(new UserRecipe(guest, r3));

        Ingredient i1 = new Ingredient("Chicken");
        i1.setIngredientid(1);
        Ingredient i2 = new Ingredient("Spices");
        i2.setIngredientid(2);
        Ingredient i3 = new Ingredient("Chicken stock");
        i3.setIngredientid(3);
        Ingredient i4 = new Ingredient("???");
        i4.setIngredientid(4);
        Ingredient i5 = new Ingredient("Corn");
        i5.setIngredientid(5);
        Ingredient i6 = new Ingredient("Kettle");
        i6.setIngredientid(6);

        r1.getIngredients().add(new RecipeIngredient(r1, i1));
        r1.getIngredients().add(new RecipeIngredient(r1, i2));
        r1.getIngredients().add(new RecipeIngredient(r1, i3));
        r2.getIngredients().add(new RecipeIngredient(r2, i4));
        r3.getIngredients().add(new RecipeIngredient(r3, i5));
        r3.getIngredients().add(new RecipeIngredient(r3, i6));

        recipes.add(r1);
        recipes.add(r2);
        recipes.add(r3);

        minimals.add(new RecipeMinimal(r1));
        minimals.add(new RecipeMinimal(r2));
        minimals.add(new RecipeMinimal(r3));

        System.out.println(minimals);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void listAuthedRecipes () throws Exception {
        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(new User("moe", "larry", "curly"));
        Mockito.when(recipeService.findMinimalsByOwner(any(User.class))).thenReturn(minimals);

        String tr = mockMvc.perform(MockMvcRequestBuilders
                .get("/recipes/all")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        String er = new ObjectMapper().writeValueAsString(minimals);

        assertEquals(er, tr);
    }

    @Test
    public void getRecipeById() throws Exception {
        Mockito.when(recipeService.findMinimalById(1L)).thenReturn(minimals.get(0));

        String tr = mockMvc.perform(MockMvcRequestBuilders
                .get("/recipes/1")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        String er = new ObjectMapper().writeValueAsString(minimals.get(0));

        assertEquals(er, tr);
    }

    @Test
    public void postNewRecipe() throws Exception {
        Mockito.when(recipeService.saveFromMinimal(any(RecipeMinimal.class))).thenReturn(recipes.get(2));

        String tr = mockMvc.perform(MockMvcRequestBuilders
                .post("/recipes/new")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(minimals.get(2))))
                .andReturn().getResponse().getContentAsString();

        String er = new ObjectMapper().writeValueAsString(minimals.get(2));

        assertEquals(er, tr);
    }

    @Test
    public void putRecipe() throws Exception {
        Mockito.when(recipeService.saveFromMinimal(any(RecipeMinimal.class))).thenReturn(recipes.get(2));

        String tr = mockMvc.perform(MockMvcRequestBuilders
                .put("/recipes/3")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(minimals.get(2))))
                .andReturn().getResponse().getContentAsString();

        String er = new ObjectMapper().writeValueAsString(minimals.get(2));

        assertEquals(er, tr);
    }

    @Test
    public void patchRecipe() throws Exception {
        Mockito.when(recipeService.updateFromMinimal(any(RecipeMinimal.class), eq(3L))).thenReturn(recipes.get(2));

        String tr = mockMvc.perform(MockMvcRequestBuilders
                .patch("/recipes/3")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(minimals.get(2))))
                .andReturn().getResponse().getContentAsString();

        String er = new ObjectMapper().writeValueAsString(minimals.get(2));

        assertEquals(er, tr);
    }

    @Test
    public void deleteRecipe() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/recipes/3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }
}
