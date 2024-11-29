package com.currencyexchange.repository;

import com.currencyexchange.model.Account;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, UUID> {

	@Query("SELECT a FROM Account a JOIN FETCH a.balances WHERE a.id = :id")
	Optional<Account> findByIdWithBalances(@Param("id") UUID id);

	Optional<Account> findByLogin(String login);

}
