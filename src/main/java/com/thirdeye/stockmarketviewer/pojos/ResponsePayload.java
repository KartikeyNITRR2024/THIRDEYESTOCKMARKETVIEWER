package com.thirdeye.stockmarketviewer.pojos;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponsePayload {
   Timestamp nextIterationTime;
   Boolean updateData;
}
