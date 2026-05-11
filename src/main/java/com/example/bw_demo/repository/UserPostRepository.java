package com.example.bw_demo.repository;

import com.example.bw_demo.entity.UserPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostRepository extends JpaRepository<UserPostEntity, Long> {
}
