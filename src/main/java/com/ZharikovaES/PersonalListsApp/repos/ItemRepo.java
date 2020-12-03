package com.ZharikovaES.PersonalListsApp.repos;

import com.ZharikovaES.PersonalListsApp.models.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepo extends CrudRepository<Item, Long> {
}
