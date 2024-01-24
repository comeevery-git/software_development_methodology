package app.domain.model.entity.payment;

import app.domain.model.entity.member.Member;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="payment")
public class Payment {
    @Id
    @Column(name = "payment_id", nullable = false)
    private String paymentId;

    @Column(name = "pay_method", nullable = false, length = 100)
    private String payMethod;

    @Column(name = "imp_u_id", nullable = false, length = 100)
    private String impUid;

    @Column(name = "merchant_u_id", nullable = false, length = 100)
    private String merchantUid;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "created_member_id")
    private Long createdMemberId;
}