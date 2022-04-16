package com.berke.subscriptionmanager.repository;

import com.berke.subscriptionmanager.entity.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Integer> {
    @Query("SELECT sp FROM ServiceProvider as sp WHERE sp.provider=?1")
    Optional<ServiceProvider> isProviderNameExist(String providerName);



}
