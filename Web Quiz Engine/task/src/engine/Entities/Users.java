package engine.Entities;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Users {


    @Id
    String email;

    @NotNull
    @NotBlank
    @Size(min = 5)
    String password;

    public Users(String email, String password){
        this.email=email;
        this.password=password;
    }

    public Users() {

    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void encryptPassword(){
        this.password= new BCryptPasswordEncoder().encode(password);
    }

}

class EmailValidation {
    public static boolean isValidation(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
}