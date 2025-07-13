package com.keepflower.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.keepflower.api.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {

}
