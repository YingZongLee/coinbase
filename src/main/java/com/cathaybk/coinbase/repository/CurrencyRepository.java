package com.cathaybk.coinbase.repository;

import com.cathaybk.coinbase.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    @Query("SELECT new Currency(id, code, name, createTime, updateTime) FROM Currency WHERE code = :code")
    Currency getByCode(@Param("code") String code);

    @Modifying
    @Query("UPDATE Currency SET code = :code, name = :name WHERE id = :id")
    void updateCurrencyById(@Param("code") String code, @Param("name") String name, @Param("id") long id);


}
