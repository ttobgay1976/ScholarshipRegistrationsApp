package com.sprms.registration.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sprms.registration.hbmbean.NdiSession;

@Repository
public interface NdiSessionRepository extends JpaRepository<NdiSession, Long> {

    NdiSession findByThreadId(String threadId);
}