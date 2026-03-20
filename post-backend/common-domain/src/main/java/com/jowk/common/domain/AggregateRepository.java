package com.jowk.common.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AggregateRepository<T extends AggregateRoot, ID>
        extends JpaRepository<T, ID> { }