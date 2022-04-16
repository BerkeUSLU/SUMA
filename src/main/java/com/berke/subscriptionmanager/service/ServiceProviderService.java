package com.berke.subscriptionmanager.service;

import com.berke.subscriptionmanager.entity.ServiceProvider;
import com.berke.subscriptionmanager.repository.ServiceProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceProviderService {
    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    public Optional<ServiceProvider> getServiceProviderById(long id) {
        return serviceProviderRepository.findById((int)id);
    }

    public List<ServiceProvider> getAllServiceProviders() {
        return serviceProviderRepository.findAll();
    }

    public Optional<ServiceProvider> updateServiceProvider(ServiceProvider serviceProvider) {
        serviceProviderRepository.save(serviceProvider);
        return getServiceProviderById(serviceProvider.getId());
    }

    public void deleteServiceProvider(ServiceProvider serviceProvider) {
        serviceProviderRepository.delete(serviceProvider);
    }

    public void deleteServiceProviderById(int id) {
        serviceProviderRepository.deleteById(id);
    }

    public boolean isExist(ServiceProvider serviceProvider) {
        return serviceProviderRepository.isProviderNameExist(serviceProvider.getProvider().getProviderName()).isPresent();
    }

}
