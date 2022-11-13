package com.jpa.interceptor.entity;

import com.jpa.interceptor.listener.RequestEntityListener;
import com.jpa.interceptor.model.RequestType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_request")
@Getter
@Setter
@EntityListeners(RequestEntityListener.class)
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "fld_id")
    private Long id;

    @Column(name = "fld_amount")
    private BigDecimal amount;

    @Column(name = "fld_type")
    private RequestType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fld_org_req_id")
    private Request original;
}
