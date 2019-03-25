package net.andreweast.services.data.restevent;

import net.andreweast.services.data.api.UserRepository;
import net.andreweast.services.data.model.Schedule;
import net.andreweast.services.data.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RepositoryEventHandler(Schedule.class)
public class ScheduleSpringDataRestEventHandler {
    private final UserRepository userRepository;

    @Autowired
    public ScheduleSpringDataRestEventHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * When a schedule is created via the REST repository, add the currently logged-in user as its creator
     * @param schedule Schedule that was passed to the REST endpoint
     */
    @HandleBeforeCreate
    @HandleBeforeSave
    public void addCreatorByUsingLoggedInUserFromSecurityContext(Schedule schedule) {
        // Only add the current logged-in user if the REST endpoint hasn't set the Creator already
        if (schedule.getCreator() == null) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            // FUTURE: There should be a more robust way to handle a not-logged in user. Since this is a security issue, and I'm using the Spring Security framework, I'll need to research best practices
            if (username == null) {
                // I'm not even sure if this will get passed to a user via HTTP...I don't know how to break the security to make this happen...
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Request was not made by a logged-in user", new Exception());
            }

            User user = this.userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new UsernameNotFoundException("Not found"));
            schedule.setCreator(user);
            // Note that no .save(schedule) is done here! This is an interceptor, and saving to the DB is done elsewhere
        }
    }
}
