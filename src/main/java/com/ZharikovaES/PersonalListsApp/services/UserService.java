package com.ZharikovaES.PersonalListsApp.services;

import com.ZharikovaES.PersonalListsApp.models.List;
import com.ZharikovaES.PersonalListsApp.models.User;
import com.ZharikovaES.PersonalListsApp.repos.UserRepo;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Service
public class UserService implements
        ApplicationListener<AuthenticationSuccessEvent>, UserDetailsService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
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

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserDetails user = userRepo.findByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("UserDetailsService returned null, which is an interface contract violation");
        }
        return user;
    }
}