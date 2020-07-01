
package studentcontroller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class UserAuth {
    private String username;
     
    private String password;
 
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
   
    public String login() {
        if(username != null && username.equals("admin") && password != null && password.equals("admin")) {
            return "index1.xhtml?faces-redirect=true";
        } else 
        {
            return "error.xhtml?faces-redirect=true";
        }
    }   
}
