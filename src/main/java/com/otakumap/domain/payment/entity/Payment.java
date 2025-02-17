package com.otakumap.domain.payment.entity;

import com.otakumap.domain.payment.enums.PaymentStatus;
import com.otakumap.domain.user.entity.User;
import com.otakumap.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payment")
public class Payment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "imp_uid", nullable = false)
    private String impUid;

    @Column(name = "merchant_uid", nullable = false)
    private String merchantUid;

    @Column(nullable = false)
    private Long amount;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

}
