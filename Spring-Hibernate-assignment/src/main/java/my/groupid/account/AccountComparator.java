package my.groupid.account;

import java.util.Comparator;

public class AccountComparator implements Comparator<Account> {
 
    @Override
    public int compare(Account account1, Account account2) {
    	
        if (account1.getEmail() == null && account2.getEmail() == null){return 0;}
        if (account1.getEmail() == null) { return -1;}
        if (account2.getEmail() == null) { return 1;}
        return account1.getEmail().compareTo(account2.getEmail());

    }
}