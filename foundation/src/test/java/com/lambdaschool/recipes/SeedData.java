package com.lambdaschool.recipes;

import com.lambdaschool.recipes.models.*;
import com.lambdaschool.recipes.services.IngredientService;
import com.lambdaschool.recipes.services.RecipeService;
import com.lambdaschool.recipes.services.RoleService;
import com.lambdaschool.recipes.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * SeedData puts both known and random data into the database. It implements CommandLineRunner.
 * <p>
 * CoomandLineRunner: Spring Boot automatically runs the run method once and only once
 * after the application context has been loaded.
 */
@Transactional
@Component
public class SeedData
        implements CommandLineRunner
{
    /**
     * Connects the Role Service to this process
     */
    @Autowired
    RoleService roleService;

    /**
     * Connects the user service to this process
     */
    @Autowired
    UserService userService;

    @Autowired
    RecipeService recipeService;

    @Autowired
    IngredientService ingredientService;

    /**
     * Generates test, seed data for our application
     * First a set of known data is seeded into our database.
     * Second a random set of data using Java Faker is seeded into our database.
     * Note this process does not remove data from the database. So if data exists in the database
     * prior to running this process, that data remains in the database.
     *
     * @param args The parameter is required by the parent interface but is not used in this process.
     */
    @Transactional
    @Override
    public void run(String[] args) throws
            Exception
    {
        userService.deleteAll();
        roleService.deleteAll();
        recipeService.deleteAll();
        ingredientService.deleteAll();

        Role r1 = new Role("admin");
        Role r2 = new Role("user");

        r1 = roleService.save(r1);
        r2 = roleService.save(r2);

        // admin, data, user
        User u1 = new User("admin",
                "password",
                "admin@lambdaschool.local");
        u1.getRoles()
                .add(new UserRoles(u1,
                        r1));
        u1.getRoles()
                .add(new UserRoles(u1,
                        r2));
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@email.local"));
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@mymail.local"));

        userService.save(u1);

        User u2 = new User("mom", "password", "mom@mom.com");
        u2.getRoles().add(new UserRoles(u2, r2));

        userService.save(u2);

        User u3 = new User("grandma", "password", "grandma@grandma.com");
        u3.getRoles().add(new UserRoles(u3, r2));

        userService.save(u3);

        Recipe rec1 = new Recipe("Alfredo", "Dad", "Boil pasta throw in alfredo sauce", "dinner");
        rec1.setOwner(u2);
        rec1.getGuests().add(new UserRecipe(u3, rec1));
        //rec1.getIngredients().add(new RecipeIngredient(rec1, new Ingredient("chicken", 1)));
        //rec1.getIngredients().add(new RecipeIngredient(rec1, new Ingredient("parmesan", 2)));
        u2.getOwnerrecipes().add(rec1);

        recipeService.saveDirect(rec1);

        Recipe rec2 = new Recipe("Chili", "Mom", "Throw in Beans and Sauce", "Lunch");
        rec2.setOwner(u3);
        //rec2.getIngredients().add(new RecipeIngredient(rec2, new Ingredient("lime")));
        //rec2.getIngredients().add(new RecipeIngredient(rec2, new Ingredient("coconut")));
        u3.getOwnerrecipes().add(rec2);

        recipeService.saveDirect(rec2);
    }
}