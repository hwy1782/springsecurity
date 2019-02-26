package com.innovativeintelli.ldapauthenticationjwttoken.security;

import com.innovativeintelli.ldapauthenticationjwttoken.BaseTests;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
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
    detailsService.loadUserByUsername("");
  }

  @Test
  public void getPersonByName() {
    List<String> names = detailsService.getPersonByName("john");
    System.out.printf(names.get(0));
  }


}