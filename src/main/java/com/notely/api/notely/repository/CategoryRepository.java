package com.notely.api.notely.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notely.api.notely.entity.Category;
import com.notely.api.notely.entity.AppUser;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
    List<Category> findByUser(AppUser user);
    Optional<Category> findByIdAndUser(Long id, AppUser user);
    List<Category> findByUserAndIsActiveTrue(AppUser user);
}
