package com.otakumap.domain.point.entity;

import com.otakumap.domain.payment.enums.PaymentStatus;
import com.otakumap.domain.user.entity.User;
import com.otakumap.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "point")
public class Point extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Long point;

    @Column(name = "charged_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime chargedAt;

    @Column(name = "charged_by")
    private String chargedBy;

    @Column(name = "merchant_uid", unique = true, nullable = false)
    private String merchantUid;

    @Column(name = "imp_uid")
    private String impUid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

}
