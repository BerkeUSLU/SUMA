package com.berke.subscriptionmanager.service;

import com.berke.subscriptionmanager.entity.Provider;
import com.berke.subscriptionmanager.exception.ServiceProviderException;
import com.berke.subscriptionmanager.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderService {
    @Autowired
    ProviderRepository providerRepository;

    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }

    public Optional<Provider> getProviderById(int id) {
        return providerRepository.findById(id);
    }

    public void newProvider(Provider provider) {
        providerRepository.save(provider);
    }

    public Provider updateProvider(Provider provider) {
        providerRepository.findById(provider.getId()).orElseThrow(
                () -> new ServiceProviderException("Not found provider to update"));

        return providerRepository.save(provider);
    }

    public void deleteProviderById(int id) {
        providerRepository.deleteById(id);
    }
}
