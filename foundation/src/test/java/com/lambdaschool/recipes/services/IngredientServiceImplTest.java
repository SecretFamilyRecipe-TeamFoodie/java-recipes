package com.lambdaschool.recipes.services;

import com.lambdaschool.recipes.RecipesApplication;
import com.lambdaschool.recipes.exceptions.ResourceNotFoundException;
import com.lambdaschool.recipes.models.Ingredient;
import com.lambdaschool.recipes.repository.IngredientRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecipesApplication.class)
public class IngredientServiceImplTest {

    @Autowired
    IngredientService ingredientService;

    @MockBean
    IngredientRepository ingredientRepository;

    @MockBean
    HelperFunctions helperFunctions;

    private List<Ingredient> ingredients = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Ingredient i1 = new Ingredient("Sugar");
        i1.setIngredientid(1);
        ingredients.add(i1);

        Ingredient i2 = new Ingredient("Spice");
        i2.setIngredientid(2);
        ingredients.add(i2);

        Ingredient i3 = new Ingredient("Everything Nice");
        i3.setIngredientid(3);
        ingredients.add(i3);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findAll() {
        Mockito.when(ingredientRepository.findAll()).thenReturn(ingredients);
        assertEquals(3, ingredientService.findAll().size());
    }

    @Test
    public void findByName() {
        Mockito.when(ingredientRepository.findByName("Sugar")).thenReturn(ingredients.get(0));
        assertEquals(1, ingredientService.findByName("Sugar").getIngredientid());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findByNameNotFound() {
        Mockito.when(ingredientRepository.findByName("Joe")).thenReturn(null);
        ingredientService.findByName("Joe");
    }

    @Test
    public void findById() {
        Mockito.when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredients.get(0)));
        assertEquals("Sugar", ingredientService.findById(1).getName());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findByIdNotFound() {
        Mockito.when(ingredientRepository.findById(1000L)).thenReturn(Optional.empty());
        assertEquals("Sugar", ingredientService.findById(1000).getName());
    }

    @Test
    public void deleteAll() {

    }
}
