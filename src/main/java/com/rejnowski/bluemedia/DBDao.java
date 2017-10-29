package com.rejnowski.bluemedia;

import com.rejnowski.bluemedia.db.model.TokenSession;
import com.rejnowski.bluemedia.db.model.User;
import com.rejnowski.bluemedia.db.model.WebsiteResource;
import com.rejnowski.bluemedia.model.UrlWithId;
import com.rejnowski.bluemedia.model.WebsiteGetResponse;
import com.rejnowski.bluemedia.utils.HibernateUtil;
import com.rejnowski.bluemedia.utils.UtilClass;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DBDao {

    public static final long SESSION_TIMEOUT_MS = 20_000;

    public Optional<String>  getToken(String login) {
        Optional<User> user = getUser(login);
        if(!user.isPresent()) return Optional.empty();
        return getToken(user.get());
    }

    public enum LoginResult{

        SUCCESS,
        LOGIN_INCORRECT,
        PASSWORD_INCORRECT

    }

    public Optional<User> getUser(String login){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("login", login));
        List<User> list = criteria.list();

        if(list.size()==1) return Optional.of(list.get(0));

        return Optional.empty();

    }

    public Optional<User> getUser(int id){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.idEq(id));
        User user = (User)criteria.uniqueResult();
        session.close();
        if(user==null) return Optional.empty();
        else return Optional.of(user);

    }

    public LoginResult tryLogin(String login, String password){

        Optional<User> userOpt = getUser(login);
        if(!userOpt.isPresent()) return LoginResult.LOGIN_INCORRECT;

        User user = userOpt.get();
        if(isPasswordCorrect(password,user.getPassword())) return login(user);
        else return LoginResult.PASSWORD_INCORRECT;

    }

    public Optional<String> getToken(User user){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Criteria criteria = session.createCriteria(TokenSession.class);
        criteria.add(Restrictions.idEq(user.getId()));
        TokenSession tokenSession = (TokenSession) criteria.uniqueResult();
        session.close();

        if(tokenSession==null) return Optional.empty();
        else return Optional.of(tokenSession.getToken());
    }

    private LoginResult login(User user) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Criteria criteria = session.createCriteria(TokenSession.class);
        criteria.add(Restrictions.idEq(user.getId()));
        TokenSession tokenSession = (TokenSession) criteria.uniqueResult();
        session.close();
        updateTimestamp(tokenSession);
        resetToken(tokenSession);
        return LoginResult.SUCCESS;
    }

    private void resetToken(TokenSession tokenSession) {
        tokenSession.setToken(UtilClass.getSha1(System.currentTimeMillis()+tokenSession.getUserId()+"bluemedia"));
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(tokenSession);
        session.getTransaction().commit();
        session.close();
    }

    private boolean isPasswordCorrect(String passwordSent, String dbPassword){
        String passwordSentHash = UtilClass.getSha1(passwordSent);
        return dbPassword.equalsIgnoreCase(passwordSentHash);
    }



    public boolean isTokenCorrect(String token){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Criteria criteria = session.createCriteria(TokenSession.class);
        criteria.add(Restrictions.eq("token",token));
        TokenSession tokenSession = (TokenSession) criteria.uniqueResult();
        session.close();

        if(tokenSession==null) return false;

        return isSessionUpToDate(tokenSession);
    }

    private boolean isSessionUpToDate(TokenSession session){
        long timestamp = session.getTimestamp();
        long now = System.currentTimeMillis();
        if((now - timestamp)<SESSION_TIMEOUT_MS){
            session.setTimestamp(now);
            updateTimestamp(session);
            return true;
        }
        else return false;
    }

    private void updateTimestamp(TokenSession tokenSession) {
        tokenSession.setTimestamp(System.currentTimeMillis());
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.update(tokenSession);
            session.getTransaction().commit();
            session.close();
    }

    public void addResource(WebsiteResource websiteResource){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(websiteResource);
        session.getTransaction().commit();
        session.close();
    }



    public List<WebsiteResource> getWebsiteResourcesByUrl(String url){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Criteria criteria = session.createCriteria(WebsiteResource.class);
        criteria.add(Restrictions.eq("pageUrl",url));
        List<WebsiteResource> websiteResources =  criteria.list();
        session.close();

        return websiteResources;

    }

    public Optional<WebsiteResource> getWebsiteResourceById(int id){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Criteria criteria = session.createCriteria(WebsiteResource.class);
        criteria.add(Restrictions.idEq(id));
        WebsiteResource websiteResource = (WebsiteResource) criteria.uniqueResult();
        session.close();
        if(websiteResource==null) return Optional.empty();
        return Optional.of(websiteResource);

    }

    public List<UrlWithId> getAllUrls(long from, long to){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(WebsiteResource.class);
        List<WebsiteResource> resources = criteria.list();
        session.close();
        List<UrlWithId> urls =new ArrayList<>();
        if(resources!=null){
//           urls= resources.stream().flatMap(WebsiteResource::getPageUrl).collect(Collectors.toList());
            for(WebsiteResource resource:resources){
                if(resource.getTimestamp()<=to &&resource.getTimestamp()>=from)
                urls.add(new UrlWithId(resource.getPageUrl(),resource.getId()));
            }
        }
        return urls;

    }

    public List<WebsiteResource> getUrlsWithSourceContainingString(String text){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        StringBuilder builder = new StringBuilder("");
        builder.append("FROM WebsiteResource WHERE ");
        builder.append("page_source");
        builder.append(" LIKE '%");
        builder.append(text);
        builder.append("%' \n");
        String hql = builder.toString();
        Query query = session.createQuery(hql);
        List<UrlWithId> urls = new ArrayList<>();
        List<WebsiteResource> resources = query.list();
//        for(WebsiteResource resource:resources) urls.add(new UrlWithId(resource.getPageUrl(),resource.getId()));
        return resources;
    }

    public List<UrlWithId> getUrlsWithSourceContainingStringFromTo(String text,long from, long to){
     List<WebsiteResource> resources = getUrlsWithSourceContainingString(text);
     List<UrlWithId> urls = new ArrayList<>();
     for(WebsiteResource resource:resources){
         if(resource.getTimestamp()>=from&&resource.getTimestamp()<=to)urls.add(new UrlWithId(resource.getPageUrl(),resource.getId()));
     }
     return urls;
    }

}
