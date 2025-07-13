package com.keepflower.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.keepflower.api.model.Session;

public interface SessionService extends JpaRepository<Session, UUID> {

}
