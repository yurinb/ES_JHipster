package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Conta;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Conta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    @Query("select conta from Conta conta where conta.user.login = ?#{principal.username}")
    List<Conta> findByUserIsCurrentUser();

}
