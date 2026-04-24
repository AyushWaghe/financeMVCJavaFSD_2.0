package org.example.services;

import org.example.dao.CategoryRepository;
import org.example.models.Category;
import org.example.models.User;
import org.example.utils.CategoryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public Category findOrCreateCategory(User user,String categoryTitle){
        String normalizedCategory;

        if(categoryTitle==null || categoryTitle.isEmpty()){
            normalizedCategory="other";
        }else {
            normalizedCategory= CategoryUtil.normalizeString(categoryTitle);
        }

        Optional<Category> category=categoryRepository.findByUser_UserIdAndTitle(user.getUserId(),normalizedCategory);

        if(category.isEmpty()){ //New category for that user to be added
            Category newCategory=new Category();
            newCategory.setUser(user);
            newCategory.setTitle(normalizedCategory);
            return categoryRepository.save(newCategory);
        }

        return category.get();
    }
}
