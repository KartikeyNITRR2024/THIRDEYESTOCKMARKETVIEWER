package com.thirdeye.stockmarketviewer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thirdeye.stockmarketviewer.entity.ConfigTable;

@Repository
public interface ConfigTableRepo extends JpaRepository<ConfigTable, Long> {
}
