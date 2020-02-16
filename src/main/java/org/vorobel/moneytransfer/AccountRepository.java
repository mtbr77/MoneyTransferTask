package org.vorobel.moneytransfer;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

@Repository//(forEntity = Account.class)
public interface AccountRepository extends EntityRepository<Account, Long> {
}
