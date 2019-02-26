package com.innovativeintelli.ldapauthenticationjwttoken.security;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.List;
import javax.naming.directory.Attributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.SearchScope;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.userdetails.InetOrgPerson;
import org.springframework.security.ldap.userdetails.Person;
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
        .searchScope(SearchScope.SUBTREE)
        .timeLimit(THREE_SECONDS)
        .countLimit(10)
        .attributes("cn")
        .base(LdapUtils.emptyLdapName())
        .where("objectclass").is("person")
        .and("sn").not().is(name)
        .and("sn").like("j*hn")

        .and("uid").isPresent();

    return ldapTemplate.search(query, new PersonAttributesMapper()).get(0);
  }


  /**
   * Custom person attributes mapper, maps the attributes to the person POJO
   */
  private class PersonAttributesMapper implements AttributesMapper<Person> {

    @Override
    public Person mapFromAttributes(Attributes attrs)
        throws NamingException, javax.naming.NamingException {
      InetOrgPerson person = new InetOrgPerson();
//      person.((String) attrs.get("cn").get());
//
//      Attribute sn = attrs.get("sn");
//      if (sn != null) {
//        person.setLastName((String) sn.get());
//      }
      return person;
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
