package com.github.kirksc1.namesake.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FooRepository extends JpaRepository<Foo, Long> {

    Optional<Foo> findByName(String name);
}
