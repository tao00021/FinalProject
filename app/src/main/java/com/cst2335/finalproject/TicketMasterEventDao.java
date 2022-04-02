package com.cst2335.finalproject;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TicketMasterEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addEventToDB(TicketMasterEvent event);

    @Query("select * from TicketMasterEvent")
    List<TicketMasterEvent> getAllEvents();

    @Query("select * from TicketMasterEvent where city = :eventCity")
    List<TicketMasterEvent> getAllEventsFromCity(String eventCity);

    @Query("delete from TicketMasterEvent where ID = :eventID")
    void removeEvent(String eventID);
}
