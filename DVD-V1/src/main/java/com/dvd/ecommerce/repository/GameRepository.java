package com.dvd.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dvd.ecommerce.model.Game;
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

}
