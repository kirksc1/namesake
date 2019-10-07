package com.github.kirksc1.namesake.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GooRepository extends JpaRepository<Goo, Long> {

    Optional<Goo> findByName(String name);
}
