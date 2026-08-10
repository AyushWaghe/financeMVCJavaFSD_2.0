package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dao.CategoryRepository;
import org.example.dao.UserRepository;
import org.example.models.Category;
import org.example.models.User;
import org.example.utils.CategoryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    @Cacheable(value = "user-categories")
    public List<Category> getCategories(Integer userId){
        return categoryRepository.findByUser_UserId(userId);
    }

    @Transactional
    @CacheEvict(value = "user-categories",key = "#userId")
    public Category createCategory(Integer userId,String categoryTitle){
        User user=userRepository.getReferenceById(userId);
            Category newCategory=new Category();
            newCategory.setUser(user);
            newCategory.setTitle(categoryTitle);
            return categoryRepository.save(newCategory);
    }
}
