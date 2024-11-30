package com.ZharikovaES.PersonalListsApp.repos;

import com.ZharikovaES.PersonalListsApp.models.Item;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ItemRepo extends CrudRepository<Item, Long> {
    @Modifying
    @Transactional
    @Query("update from Item i set i.isMarked = :isMarked where i.id = :id")
    void updateCheck(Long id, Boolean isMarked);

}
