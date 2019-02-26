package com.innovativeintelli.ldapauthenticationjwttoken.security;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.List;
import javax.naming.directory.Attributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author hongweiye
 * @Description //TODO
 * @Time 2019/2/26 16:22
 */
@Service
public class LdapUserDetailsService implements UserDetailsService {

  private static final Integer THREE_SECONDS = 3000;

  @Autowired
  private LdapTemplate ldapTemplate;

  @Override
  /**
   * 查询方法参考：https://memorynotfound.com/spring-boot-spring-ldap-advanced-ldap-queries-example/
   */
  public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
    LdapQuery query = query()
        // 返回的属性
        .attributes("cn", "uid")
        .where("objectclass").is("person")
        .and("uid").is(name);

    return ldapTemplate.search(query, new UserAttrMapper()).get(0);
  }


  /**
   * Custom person attributes mapper, maps the attributes to the person POJO
   */
  private class UserAttrMapper implements AttributesMapper<UserDetails> {

    @Override
    public UserDetails mapFromAttributes(Attributes attrs)
        throws NamingException, javax.naming.NamingException {
      String userName = (String) attrs.get("cn").get();
      UserDetails userDetails = User
          .withUsername(userName)
          .password("")
          .authorities("")
          .build();
      return userDetails;
//      person.((String) attrs.get("cn").get());
//
//      Attribute sn = attrs.get("sn");
//      if (sn != null) {
//        person.setLastName((String) sn.get());
//      }
//      return user;
    }
  }

  public List<String> getPersonByName(String name) {

    LdapQuery query = query()
        // 返回的属性
        .attributes("cn", "uid")
        .where("objectclass").is("person")
        .and("uid").is(name);

    //查询每一个entry的"cn"值
    return ldapTemplate
        .search(query, (AttributesMapper<String>) attrs -> (String) attrs.get("cn").get());
  }


}
