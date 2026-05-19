package com.umcsuser.carrent.repositories.impl.jpa;

import com.umcsuser.carrent.models.Rental;
import com.umcsuser.carrent.repositories.RentalJpaRepository;
import com.umcsuser.carrent.repositories.RentalRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class RentalRepositoryJpaAdapter implements RentalRepository {
    private final RentalJpaRepository delegate;

    public RentalRepositoryJpaAdapter(RentalJpaRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<Rental> findAll() {
        return delegate.findAll();
    }

    @Override
    public Optional<Rental> findById(String id) {
        return delegate.findById(id);
    }

    @Override
    public Rental save(Rental rental) {
        return delegate.save(rental);
    }

    @Override
    public void deleteById(String id) {
        delegate.deleteById(id);
    }

    @Override
    public Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleId) {
        return delegate.findByVehicle_IdAndReturnDateTimeIsNull(vehicleId);
    }

    @Override
    public Optional<Rental> findByUserIdAndReturnDateIsNull(String userId) {
        return delegate.findByUser_IdAndReturnDateTimeIsNull(userId);
    }
}
