package com.artwork.mori.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    //exclusive lock cannot be obtained if shared locks are present
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Resource r WHERE r.id = :id")
    Resource getByIdAndLock(@Param("id") Long orgId);

    //shared lock waits till exclusive lock is released (if its present)
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT r FROM Resource r WHERE r.id = :id")
    Resource getByIdWithPessimisticRead(@Param("id") Long orgId);
}
