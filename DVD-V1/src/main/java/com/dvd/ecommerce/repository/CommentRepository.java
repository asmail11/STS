package com.dvd.ecommerce.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dvd.ecommerce.model.Comment;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


}
