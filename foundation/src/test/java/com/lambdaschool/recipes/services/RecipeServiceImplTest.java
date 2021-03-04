package com.lambdaschool.recipes.services;

import com.lambdaschool.recipes.RecipesApplication;
import com.lambdaschool.recipes.exceptions.ResourceNotFoundException;
import com.lambdaschool.recipes.models.*;
import com.lambdaschool.recipes.repository.RecipeRepository;
import com.lambdaschool.recipes.services.HelperFunctions;
import com.lambdaschool.recipes.services.RecipeService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecipesApplication.class)
@AutoConfigureMockMvc
@WithMockUser(username = "admin",
        roles = {"USER", "ADMIN"})
public class RecipeServiceImplTest {

    @Autowired
    RecipeService recipeService;

    @MockBean
    RecipeRepository recipeRepository;

    @MockBean
    HelperFunctions helperFunctions;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private List<Recipe> recipes = new ArrayList<>();

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
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findAll() {
        Mockito.when(recipeRepository.findAll()).thenReturn(recipes);
        assertEquals(3, recipeService.findAll().size());
    }

    @Test
    public void findAllMinimals() {
        Mockito.when(recipeRepository.findAll()).thenReturn(recipes);
        assertEquals(3, recipeService.findAllMinimals().size());
    }

    @Test
    public void findById() {
        Mockito.when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipes.get(0)));
        assertEquals("spicy chicken soup", recipeService.findById(1L).getName());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findByIdNotFound() {
        Mockito.when(recipeRepository.findById(11L)).thenReturn(Optional.empty());
        assertEquals("spicy chicken soup", recipeService.findById(11L).getName());
    }

    @Test
    public void findMinimalById() {
        Mockito.when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipes.get(0)));
        assertEquals("spicy chicken soup", recipeService.findMinimalById(1L).getName());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findMinimalByIdNotFound() {
        Mockito.when(recipeRepository.findById(11L)).thenReturn(Optional.empty());
        assertEquals("spicy chicken soup", recipeService.findMinimalById(11L).getName());
    }

    @Test
    public void findByName() {
        Mockito.when(recipeRepository.findByName("spicy chicken soup")).thenReturn(recipes.get(0));
        assertEquals(1, recipeService.findByName("spicy chicken soup").getRecipeid());
    }

    @Test
    public void findByNameNotFound() {
        Mockito.when(recipeRepository.findByName("snarfblat")).thenReturn(null);
        assertNull(recipeService.findByName("snarfblat"));
    }

    @Test
    public void findMinimalByName() {
        Mockito.when(recipeRepository.findByName("spicy chicken soup")).thenReturn(recipes.get(0));
        assertEquals(1, recipeService.findMinimalByName("spicy chicken soup").getRecipeid());
    }

    @Test(expected = NullPointerException.class)
    public void findMinimalByNameNotFound() {
        Mockito.when(recipeRepository.findByName("snarfblat")).thenReturn(null);
        assertNull(recipeService.findMinimalByName("snarfblat"));
    }

    @Test
    public void findByOwner() {
        User owner = new User("owner", "ownerpass", "owneremail");
        owner.setUserid(1);

        Mockito.when(recipeRepository.findAllByOwner(owner)).thenReturn(recipes);
        assertEquals(3, recipeService.findByOwner(owner).size());
    }

    @Test
    public void findMinimalsByOwner() {
        User owner = new User("owner", "ownerpass", "owneremail");
        owner.setUserid(1);

        Mockito.when(recipeRepository.findAllByOwner(owner)).thenReturn(recipes);
        assertEquals(3, recipeService.findMinimalsByOwner(owner).size());
    }

    @Test
    public void findByOwnerOrGuest() {
        // todo NYI
    }

    @Test
    public void findMinimalsByOwnerOrGuest() {
        // todo NYI
    }

    @Test
    public void save() {
        Recipe r4 = new Recipe("john", "jacob", "jingleheimer", "schmidt");

        Mockito.when(recipeRepository.save(any(Recipe.class))).thenReturn(r4);
        assertEquals("john", recipeService.save(r4).getName());
    }

    @Test
    public void saveFromMinimal() {
        Recipe r4 = new Recipe("john", "jacob", "jingleheimer", "schmidt");

        RecipeMinimal r4m = new RecipeMinimal(r4);

        Mockito.when(recipeRepository.save(any(Recipe.class))).thenReturn(r4);
        assertEquals("john", recipeService.saveFromMinimal(r4m).getName());
    }

    @Test
    public void saveDirect() {
    }

    @Test
    public void update() {
        Recipe r4 = new Recipe("john", "jacob", "jingleheimer", "schmidt");
        r4.setRecipeid(4);

        Mockito.when(recipeRepository.save(any(Recipe.class))).thenReturn(r4);
        Mockito.when(recipeRepository.findById(4L)).thenReturn(Optional.of(r4));
        assertEquals("john", recipeService.update(r4, 4L).getName());
    }

    @Test
    public void updateFromMinimal() {
        Recipe r4 = new Recipe("john", "jacob", "jingleheimer", "schmidt");
        RecipeMinimal r4m = new RecipeMinimal(r4);
        r4.setRecipeid(4);

        Mockito.when(recipeRepository.save(any(Recipe.class))).thenReturn(r4);
        Mockito.when(recipeRepository.findById(4L)).thenReturn(Optional.of(r4));
        assertEquals("john", recipeService.update(r4, 4L).getName());
    }

    @Test
    public void deleteById() {
    }

    @Test
    public void deleteAll() {
    }
}
