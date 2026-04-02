package com.jowk.parcel.history;

import com.jowk.parcel.history.dto.ParcelHistorySummary;
import java.util.List;

public interface ParcelHistoryService {

    List<ParcelHistorySummary> getLatestHistory(String trackingNumber);

}
