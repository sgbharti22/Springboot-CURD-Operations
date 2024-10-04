package com.bmt.webapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bmt.webapp.models.Clients;
@Repository
public interface ClientRepository extends JpaRepository<Clients, Integer> {
    public Clients findByEmail(String email);
}
