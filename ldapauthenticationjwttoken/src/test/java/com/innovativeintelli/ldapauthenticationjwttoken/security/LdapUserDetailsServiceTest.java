package com.innovativeintelli.ldapauthenticationjwttoken.security;

import com.innovativeintelli.ldapauthenticationjwttoken.BaseTests;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @author hongweiye
 * @Description //TODO
 * @Time 2019/2/26 17:19
 */
public class LdapUserDetailsServiceTest extends BaseTests {

  @Resource
  private LdapUserDetailsService detailsService;

  @Test
  public void loadUserByUsername() {
    UserDetails userDetails = detailsService.loadUserByUsername("john");
    System.out.printf(userDetails.getUsername());
  }

  @Test
  public void getPersonByName() {
    List<String> names = detailsService.getPersonByName("john");
    System.out.printf(names.get(0));
  }


}