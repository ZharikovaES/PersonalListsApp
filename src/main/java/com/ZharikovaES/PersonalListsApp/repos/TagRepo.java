package com.ZharikovaES.PersonalListsApp.repos;

import com.ZharikovaES.PersonalListsApp.models.List;
import com.ZharikovaES.PersonalListsApp.models.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepo extends CrudRepository<Tag, Long> {
//    @Modifying
//    @Query("from List l, User u where u.username = :username and u.id = l.listId")
//    List findListByUsername(@Param("username") String username);

    //    @Query("select l from List l where l.listId = :id")

    @Query("select t.id from Tag t where t.name = :name")
    Long findIdByName(String name);

    Tag findByName(String name);

    java.util.List<Tag> findAll();
}
