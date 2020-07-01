package studentcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.security.auth.Subject;
import model.Address;
import model.Student;
import model.Subjects;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.HibernateUtil;

@ManagedBean
@RequestScoped
public class Handler {

    String searchItem;
    String studentAddress;
    Student student = new Student();
    List<Student> list = new ArrayList();
    private Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

    @PostConstruct
    public void init() {
        Transaction t = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            t = session.beginTransaction();
            Query q = session.createQuery("select student from Student student");
            list = q.list();
            t.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void searchItem(String type) {
        Transaction t = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            t = session.beginTransaction();
            if (type.equals("id")) {
                Query q = session.createQuery("select s from Student s where s.studentid=:id");
                q.setParameter("id", searchItem);
                list = q.list();
            }
            if (type.equals("name")) {
                Query q = session.createQuery("select s from Student s where s.name=:name");
                q.setParameter("name", searchItem);
                list = q.list();
            }
            t.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String addStudent() {
        Transaction tx = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            tx = session.beginTransaction();
            session.save(student);
            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        init();
        return "index1.xhtml?faces-redirect=true";
    }

    public void deletedata(Student student) {
        Transaction tx = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            tx = session.beginTransaction();
            session.delete(student);
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        init();
    }

    public String viewDetails(Student student) {
        sessionMap.put("viewStudent", student);
        return "viewdetails.xhtml?faces-redirect=true";
    }

    public String edit(Student student) {
        sessionMap.put("editStudent", student);
        return "editStudent.xhtml?faces-redirect=true";
    }

    public String update(Student student) {
        Transaction tx = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            tx = session.beginTransaction();
            session.merge(student);
            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        init();
        return "index1.xhtml?faces-redirect=true";
    }

    public void addSubject(Student student, String subject) {
        Subjects subjects = new Subjects();
        subjects.setStudent(student);
        subjects.setSubject(subject);
        Transaction tx = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            tx = session.beginTransaction();
            session.merge(subjects);
            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void skipSubject(Subjects subject) {
        Transaction tx = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            tx = session.beginTransaction();
            session.delete(subject);
            tx.commit();
            getSubAdd(subject.getStudent());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAddress(Address add) {
        Transaction tx = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            tx = session.beginTransaction();
            session.delete(add);
            tx.commit();
            getSubAdd(add.getStudent());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAddress(Student student) {
        Address address = new Address();
        address.setStudent(student);
        address.setAddress(studentAddress);
        Transaction tx = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            tx = session.beginTransaction();
            session.save(address);
            tx.commit();
            studentAddress = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String gotoDashboard(Student student) {
        getSubAdd(student);
        return "dashboard.xhtml?faces-redirect=true";
    }

    public void getSubAdd(Student student) {
        List<Subject> sublist;
        List<Address> addlist;
        Transaction t = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            t = session.beginTransaction();
            Query q = session.createQuery("Select a from Address a where a.student.id= :id");
            q.setParameter("id", student.getId());
            addlist = q.list();
            t.commit();
            sessionMap.put("addlist", addlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            t = session.beginTransaction();
            Query q = session.createQuery("Select DISTINCT s from Subjects s where s.student.id= :id");
            q.setParameter("id", student.getId());
            sublist = q.list();
            t.commit();
            sessionMap.put("sublist", sublist);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String editAddress(Address add) {
        sessionMap.put("editAddress", add);
        studentAddress=null;
        return "editaddress.xhtml?faces-redirect=true";
    }
    
    public String updateAddress(Address add){
        add.setAddress(studentAddress);
        Transaction tx = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            tx = session.beginTransaction();
            session.merge(add);
            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    return "dashboard.xhtml?faces-redirect=true";
    }
// getter setters   

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public List<Student> getList() {
        return list;
    }

    public void setList(List<Student> list) {
        this.list = list;
    }

    public String getSearchItem() {
        return searchItem;
    }

    public void setSearchItem(String searchItem) {
        this.searchItem = searchItem;
    }

    public String getStudentAddress() {
        return studentAddress;
    }

    public void setStudentAddress(String studentAddress) {
        this.studentAddress = studentAddress;
    }

    public Map<String, Object> getSessionMap() {
        return sessionMap;
    }

    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }

}
