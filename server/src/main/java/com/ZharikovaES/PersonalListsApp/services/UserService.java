package com.ZharikovaES.PersonalListsApp.services;

import com.ZharikovaES.PersonalListsApp.models.List;
import com.ZharikovaES.PersonalListsApp.models.User;
import com.ZharikovaES.PersonalListsApp.repos.UserRepo;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class UserService implements
        ApplicationListener<AuthenticationSuccessEvent>, UserDetailsService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void onApplicationEvent(@SuppressWarnings("null") AuthenticationSuccessEvent event) {
        String userName = ((UserDetails) event.getAuthentication().
                getPrincipal()).getUsername();
        User user = userRepo.findByUsername(userName);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(event.getTimestamp());
        user.setTimezoneID(calendar.getTimeZone().getID());
        for (List l : user.getLists()) {
            l.setDateUpdateTZByUser();
        }
        user.setDateLastActivity(new Date());
        userRepo.save(user);
    }

    public boolean existsUserByUsername(String s) {
        User user = userRepo.findByUsername(s);

        return user != null;
    }

    @Override
    public User loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("UserDetailsService returned null, which is an interface contract violation");
        }
        return user;
    }

    public User addNewUser(User user) {
      return userRepo.save(user);
    }

    public User activateUser(String code) {
        User user = userRepo.findByActivationCode(code);

        if (user == null) {
            return null;
        }
        user.setActivationCode(null);
        userRepo.save(user);
        return user;
    }
}