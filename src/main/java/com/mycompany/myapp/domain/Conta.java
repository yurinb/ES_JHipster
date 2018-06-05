package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Conta.
 */
@Entity
@Table(name = "conta")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Conta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conta")
    private String conta;

    @Column(name = "valor")
    private Long valor;

    @Column(name = "dia_vencimento")
    private Integer diaVencimento;

    @Column(name = "num_parcelas")
    private Integer numParcelas;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConta() {
        return conta;
    }

    public Conta conta(String conta) {
        this.conta = conta;
        return this;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public Long getValor() {
        return valor;
    }

    public Conta valor(Long valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }

    public Integer getDiaVencimento() {
        return diaVencimento;
    }

    public Conta diaVencimento(Integer diaVencimento) {
        this.diaVencimento = diaVencimento;
        return this;
    }

    public void setDiaVencimento(Integer diaVencimento) {
        this.diaVencimento = diaVencimento;
    }

    public Integer getNumParcelas() {
        return numParcelas;
    }

    public Conta numParcelas(Integer numParcelas) {
        this.numParcelas = numParcelas;
        return this;
    }

    public void setNumParcelas(Integer numParcelas) {
        this.numParcelas = numParcelas;
    }

    public User getUser() {
        return user;
    }

    public Conta user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Conta conta = (Conta) o;
        if (conta.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), conta.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Conta{" +
            "id=" + getId() +
            ", conta='" + getConta() + "'" +
            ", valor=" + getValor() +
            ", diaVencimento=" + getDiaVencimento() +
            ", numParcelas=" + getNumParcelas() +
            "}";
    }
}
