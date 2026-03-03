package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "CommentImage", uniqueConstraints = {
    @UniqueConstraint(name = "uq_comment_image", columnNames = {"commentId", "commentUrl"})
})
public class CommentImage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "commentId", nullable = false)
  private Comment comment;

  @Column(nullable = false, length = 200)
  private String commentUrl;
}
