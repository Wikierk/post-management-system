package com.jowk.parcel.history.impl;

import com.jowk.parcel.history.ParcelHistoryRepository;
import com.jowk.parcel.history.dto.ParcelStatusChangedEvent;
import com.jowk.parcel.history.entity.ParcelHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Component
@RequiredArgsConstructor
public class ParcelStatusChangedEventListener {

    private final ParcelHistoryRepository parcelHistoryRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onParcelStatusChanged(ParcelStatusChangedEvent event) {
        ParcelHistory parcelHistory = new ParcelHistory(
                event.trackingNumber(),
                event.status(),
                event.description(),
                event.actorId(),
                event.logisticHolder()
        );
        parcelHistoryRepository.save(parcelHistory);
    }

}

