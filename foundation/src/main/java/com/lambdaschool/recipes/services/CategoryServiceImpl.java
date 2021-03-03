package com.lambdaschool.recipes.services;

import com.lambdaschool.recipes.exceptions.ResourceNotFoundException;
import com.lambdaschool.recipes.models.Category;
import com.lambdaschool.recipes.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "categoryService")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryrepos;

    @Override
    public Category findCategoryById(long id) throws ResourceNotFoundException {
        return categoryrepos.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category id " + id + " not found."));
    }

    @Override
    public List<Category> findByNameContaining(String name) {
        return categoryrepos.findCategoryContainingIgnoreCase(name.toLowerCase());
    }

    @Override
    public List<Category> findAll() {
        List<Category> list = new ArrayList<>();
        categoryrepos.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        categoryrepos.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category id " + id + " not found."));
        categoryrepos.deleteById(id);
    }

    @Override
    public Category findByName(String name) {
        Category cat = categoryrepos.findCategoryByName(name.toLowerCase());
        if (cat == null) {
            throw new ResourceNotFoundException("Category name " + name + "not found.");
        }
        return cat;
    }

    @Override
    public Category save(Category category) {
        return null;
    }

    @Override
    public Category update(Category category, long id) {
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void deleteAll() {
        categoryrepos.deleteAll();
    }
}
