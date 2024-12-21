package com.thirdeye.stockmarketviewer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thirdeye.stockmarketviewer.entity.ConfigUsed;

@Repository
public interface ConfigUsedRepo extends JpaRepository<ConfigUsed, Long> {
}
