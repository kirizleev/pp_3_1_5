package ru.kata.spring.boot_security.demo.dao;

import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao, UserDetailsService {

   private EntityManager entityManager;

   @PersistenceContext
   public void setEntityManager(EntityManager entityManager) {
      this.entityManager = entityManager;
   }

   @Override
   public void add(User user) {
      user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
      entityManager.persist(user);
   }

   @Override
   public void delete(User user) {
      entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));
   }

   @Override
   public void edit(User user) {
      entityManager.merge(user);
   }

   @Override
   public User getById(Long id) {
      return entityManager.find(User.class, id);
   }

   @Override
   @RequestMapping(method = RequestMethod.GET)
   public List<User> listUsers() {
      TypedQuery<User> query=entityManager.createQuery("from User", User.class);
      return query.getResultList();
   }

   @Override
   @Transactional
   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      User user = entityManager.createQuery("SELECT p FROM User p JOIN FETCH p.roles WHERE email = '" + email + "'", User.class).getSingleResult();
      if (user == null) {
         throw new UsernameNotFoundException("User not found");
      }
      return user;
   }
}
