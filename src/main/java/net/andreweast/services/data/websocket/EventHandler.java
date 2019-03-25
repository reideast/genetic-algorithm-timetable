package net.andreweast.services.data.websocket;

import net.andreweast.services.data.model.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static net.andreweast.WebSocketConfiguration.MESSAGE_PREFIX;

@Component
@RepositoryEventHandler(Schedule.class)
public class EventHandler {
    private final SimpMessagingTemplate websocket;

    private final EntityLinks entityLinks;

    @Autowired
    public EventHandler(SimpMessagingTemplate websocket, EntityLinks entityLinks) {
        this.websocket = websocket;
        this.entityLinks = entityLinks;
    }

    @HandleAfterCreate
    public void newSchedule(Schedule schedule) {
        this.websocket.convertAndSend(MESSAGE_PREFIX + "/newSchedule", getPath(schedule));
    }

    @HandleAfterDelete
    public void deleteSchedule(Schedule schedule) {
        this.websocket.convertAndSend(MESSAGE_PREFIX + "/deleteSchedule", getPath(schedule));
    }

    @HandleAfterSave
    public void updateSchedule(Schedule schedule) {
        this.websocket.convertAndSend(MESSAGE_PREFIX + "/updateSchedule", getPath(schedule));
    }

    /**
     * Starting with a {@link Schedule}, get the HATEOAS link using spring-data EntityLink
     */
    private String getPath(Schedule schedule) {
        return this.entityLinks.linkForSingleResource(schedule.getClass(), schedule.getScheduleId()).toUri().getPath();
    }
}
