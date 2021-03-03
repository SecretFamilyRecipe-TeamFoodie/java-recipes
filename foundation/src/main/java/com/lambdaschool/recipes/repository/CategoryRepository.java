package com.lambdaschool.recipes.repository;

import com.lambdaschool.recipes.models.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    Category findCategoryByName(String name);

    List<Category> findCategoryContainingIgnoreCase(String name);

    Category addCategory();

}
