package org.vorobel.moneytransfer;

import io.micronaut.data.annotation.*;
import io.micronaut.data.model.*;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long>{
}
