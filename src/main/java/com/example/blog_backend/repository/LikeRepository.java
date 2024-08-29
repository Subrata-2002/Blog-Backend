package com.example.blog_backend.repository;

import com.example.blog_backend.entity.Like;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByArticleIdAndUserId(Long articleId, Long userId);
}
