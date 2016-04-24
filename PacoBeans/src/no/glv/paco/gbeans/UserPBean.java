package no.glv.paco.gbeans;

public class UserPBean extends PacoBean {
    
    private String password;

    public UserPBean( String user, String pwd ) {
        super( user );
        
        assert pwd != null;
        password = pwd;
    }
    
    public boolean validatePWD( String pwd ) {
        if (pwd == null || pwd.length() == 0 ) return false;
        
        return password.equals( pwd );
    }
    
}
