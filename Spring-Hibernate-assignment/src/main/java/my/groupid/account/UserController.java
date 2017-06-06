package my.groupid.account;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@Autowired
	private AccountService accountService;	

	@GetMapping("/users")
	@ResponseStatus(value = HttpStatus.OK)
	// @Secured({"ROLE_USER", "ROLE_ADMIN"})
	public List<Account> getUsers() {
		
		List <Account> accountList = accountService.getUsers();
		Collections.sort(accountList, new AccountComparator ());
		return accountList;
	}
}
