package com.thirdeye.stockmarketviewer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "MARKET_THRESOLD")
@NoArgsConstructor
@Getter
@Setter
public class MarketThresold {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Column(name="userid", nullable=false)
	private Long userId;
	
	@Column(name="Thresold_time", nullable=false)
	private Long thresoldTime;
	     
	@Column(name="Thresold_price", nullable=false)
	private Double thresoldPrice;
	
	@Column(name="Thresold_type", nullable=false)
	private Integer thresoldType;
	
}

