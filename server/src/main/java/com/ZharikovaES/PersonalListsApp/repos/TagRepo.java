package com.ZharikovaES.PersonalListsApp.repos;

import com.ZharikovaES.PersonalListsApp.models.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepo extends CrudRepository<Tag, Long> {

    @Query("select t.id from Tag t where t.name = :name")
    Long findIdByName(String name);

    Tag findByName(String name);

    java.util.List<Tag> findAll();
}
