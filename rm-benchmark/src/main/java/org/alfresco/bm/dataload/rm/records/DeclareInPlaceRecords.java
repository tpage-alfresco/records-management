/*
 * Copyright (C) 2005-2017 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.bm.dataload.rm.records;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import org.alfresco.bm.dataload.RMBaseEventProcessor;
import org.alfresco.bm.event.Event;
import org.alfresco.bm.event.EventResult;
import org.alfresco.rest.core.RestAPIFactory;
import org.alfresco.utility.model.UserModel;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

/**
 * Timed event that declares as record pre-scheduled file
 *
 * @author Ana Bozianu
 * @since 2.6
 */
public class DeclareInPlaceRecords extends RMBaseEventProcessor
{
    public static final String INVALID_DATA_MSG_TEMPLATE = "This processor requires data with fields: {0}, {1}, {2}.";
    public static final long DEFAULT_DECLARE_RECORDS_DELAY = 10000L;
    private long declareInPlaceRecordDelay = DEFAULT_DECLARE_RECORDS_DELAY;
    public static final String DEFAULT_EVENT_NAME_IN_PLACE_RECORD_DECLARED = "inPlaceRecordDeclared";
    private String eventNameInPlaceRecordsDeclared = DEFAULT_EVENT_NAME_IN_PLACE_RECORD_DECLARED;

    @Autowired
    private RestAPIFactory restAPIFactory;

    public void setEventNameInPlaceRecordsDeclared(String eventNameInPlaceRecordsDeclared)
    {
        this.eventNameInPlaceRecordsDeclared = eventNameInPlaceRecordsDeclared;
    }

    public String getEventNameInPlaceRecordsDeclared()
    {
        return eventNameInPlaceRecordsDeclared;
    }

    public void setDeclareInPlaceRecordDelay(long declareInPlaceRecordDelay)
    {
        this.declareInPlaceRecordDelay = declareInPlaceRecordDelay;
    }

    @Override
    protected EventResult processEvent(Event event) throws Exception
    {
        StringBuilder eventOutputMsg = new StringBuilder("Declaring file as record: \n");
        super.suspendTimer();

        if (event == null)
        {
            throw new IllegalStateException("This processor requires an event.");
        }

        DBObject dataObj = (DBObject) event.getData();
        if (dataObj == null)
        {
            throw new IllegalStateException(MessageFormat.format(INVALID_DATA_MSG_TEMPLATE, FIELD_ID, FIELD_USERNAME, FIELD_PASSWORD));
        }
        String id = (String) dataObj.get(FIELD_ID);
        String username = (String) dataObj.get(FIELD_USERNAME);
        String password = (String) dataObj.get(FIELD_PASSWORD);

        if (isBlank(id) || isBlank(username) || isBlank(password))
        {
            throw new IllegalStateException(MessageFormat.format(INVALID_DATA_MSG_TEMPLATE, FIELD_ID, FIELD_USERNAME, FIELD_PASSWORD));
        }

        // Call the REST API
        try
        {
            super.resumeTimer();
            restAPIFactory.getFilesAPI(new UserModel(username, password)).declareAsRecord(id);
            String statusCode = restAPIFactory.getRmRestWrapper().getStatusCode();
            super.suspendTimer();
            TimeUnit.MILLISECONDS.sleep(declareInPlaceRecordDelay);

            if(HttpStatus.valueOf(Integer.parseInt(statusCode)) == HttpStatus.CREATED)
            {
                eventOutputMsg.append("success");
            }
            else
            {
                eventOutputMsg.append("Failed with code " + statusCode + ".\n " +
                                       restAPIFactory.getRmRestWrapper().assertLastError().getBriefSummary() + ". \n" +
                                       restAPIFactory.getRmRestWrapper().assertLastError().getStackTrace());
            }

            return new EventResult(eventOutputMsg.toString(), new Event(getEventNameInPlaceRecordsDeclared(), dataObj));
        }
        catch (Exception e)
        {
            String error = e.getMessage();
            String stack = ExceptionUtils.getStackTrace(e);
            // Grab REST API information
            DBObject data = BasicDBObjectBuilder.start()
                    .append("error", error)
                    .append(FIELD_ID, id)
                    .append(FIELD_USERNAME, username)
                    .append(FIELD_PASSWORD, password)
                    .append("stack", stack).get();
            // Build failure result
            return new EventResult(data, false);
        }
    }

}
