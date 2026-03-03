package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Comment")
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long commentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userName", nullable = false)
  private Buyer buyer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "skuId")
  private Sku sku;

  private Integer ratings; // 1–5 or null

  @Column(length = 500)
  private String content;

  // Self-referential: replies to another comment
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parentCommentId")
  private Comment parentComment;
}
