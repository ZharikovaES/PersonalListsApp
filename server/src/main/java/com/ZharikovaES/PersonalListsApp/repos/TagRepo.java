package com.ZharikovaES.PersonalListsApp.repos;

import com.ZharikovaES.PersonalListsApp.models.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepo extends CrudRepository<Tag, Long> {

    @Query("select t.id from Tag t where t.name = :name")
    Long findIdByName(@Param("name") String name);

    Tag findByName(@Param("name") String name);

    java.util.List<Tag> findAll();
}
