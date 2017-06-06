package my.groupid.account;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserListController {

    @Secured({"ROLE_ADMIN"})
	@RequestMapping(value = {"/accoutlist" }, method = RequestMethod.GET)	
	String accoutlist() {
		return "accountlist";
	}
}
