package my.groupid.account;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class AccountController {

    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @CacheEvict(value="accounts", allEntries=true)
    @GetMapping("account/current")
    @ResponseStatus(value = HttpStatus.OK)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public Account currentAccount(Principal principal) {
        Assert.notNull(principal);
        return accountRepository.findOneByEmail(principal.getName());
    }

    @GetMapping("account/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public Account account(@PathVariable("id") Long id) {
        return accountRepository.findOne(id);
    }

    private static void setFilenameTimestamp(final HttpServletResponse httpServletResponse) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMDD_HHmmss");
        final String formattedDate = simpleDateFormat.format(new Date());
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + formattedDate + '.' + "csv" + "\"");
    }

    @GetMapping(value = "accounts/csv", produces = "text/csv")
    public void accounts2(final HttpServletResponse httpServletResponse) throws IOException {
        final List<Account> all = accountRepository.findAll();
        if (all.isEmpty()) {
            return;
        }
        try {
            setFilenameTimestamp(httpServletResponse);

            final CsvMapper csvMapper = new CsvMapper();
            final CsvSchema csvSchema = csvMapper.schemaFor(all.get(0).getClass()).withHeader();

            final ObjectWriter objectWriter = csvMapper.writer(csvSchema);

            File tempFile = new File("users.csv");
            FileOutputStream tempFileOutputStream = new FileOutputStream(tempFile);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(tempFileOutputStream, 1024);
            OutputStreamWriter writerOutputStream = new OutputStreamWriter(bufferedOutputStream, "UTF-8");
            objectWriter.writeValue(httpServletResponse.getOutputStream(), all);


        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }
}
