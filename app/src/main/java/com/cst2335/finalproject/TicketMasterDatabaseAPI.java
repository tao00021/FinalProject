package com.cst2335.finalproject;

import android.content.Context;

import java.util.List;
/**
 * This API is used to retrieve and save items to the database
 */
public class TicketMasterDatabaseAPI {
    private ProjectDatabase database;
    public TicketMasterDatabaseAPI(Context context){
        database=ProjectDatabase.getDatabase(context);
    }
    public List<TicketMasterEvent> getAllEventsForCity(String city)
    {
        return database.eventDao().getAllEventsFromCity(city);
    }
    public void saveNewEvent(TicketMasterEvent newEvent)
    {
        database.eventDao().addEventToDB(newEvent);
    }
    public void removeEventsFromCity(String city)
    {
        database.eventDao().removeEventsFromCity(city);
    }
    public List<TicketMasterEvent> getAllEvents()
    {
        return database.eventDao().getAllEvents();
    }

}
