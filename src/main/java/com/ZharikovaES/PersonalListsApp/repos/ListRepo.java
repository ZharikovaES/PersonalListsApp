package com.ZharikovaES.PersonalListsApp.repos;

import com.ZharikovaES.PersonalListsApp.models.List;
import com.ZharikovaES.PersonalListsApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ListRepo extends CrudRepository<List, Long> {
//    @Modifying
//    @Query("from List l, User u where u.username = :username and u.id = l.listId")
//    List findListByUsername(@Param("username") String username);

    java.util.List<List> findAll();

}
