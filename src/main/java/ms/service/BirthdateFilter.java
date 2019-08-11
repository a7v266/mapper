package ms.service;

import java.util.Date;

public interface BirthdateFilter {

    String BIRTHDATE = "birthdate";

    void filterBirthdateGt(Date date);

    void filterBirthdateGe(Date date);

    void filterBirthdateLt(Date date);

    void filterBirthdateLe(Date date);

}
