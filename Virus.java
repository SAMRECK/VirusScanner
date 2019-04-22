
package virus_scanner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Virus 
{
    public String path;
    
    public Virus(String filepath) throws NoSuchAlgorithmException
    {
 
        path = filepath;
        
        System.out.println(path);
    
    
        if (path == null)
        {
            JOptionPane.showMessageDialog(null, "Please Select a File using BROWSE Button");
        }      
           
        MessageDigest md = MessageDigest.getInstance("MD5");
        
        FileInputStream fis = null;
        
        try {
            fis = new FileInputStream(path);
        
        } catch (FileNotFoundException ex) {
        
            Logger.getLogger(Virus.class.getName()).log(Level.SEVERE, null, ex);
        
        }

        byte[] dataBytes = new byte[1024];
        

        int nread = 0;
        try {
        
            while ((nread = fis.read(dataBytes)) != -1)
            {
        
                md.update(dataBytes, 0, nread);
        
            }
        } catch (IOException ex) {
            Logger.getLogger(Virus.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] mdbytes = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) 
        {
            sb.append(Integer.toString((mdbytes[i] & 0xff), 16));
        }

        

        
        String hash = sb.toString();
        
        System.out.println("Digest(in hex format):: " + hash);
        
        
        
        String sql = "SELECT * FROM hashing WHERE hash = ?";
        
        try
        {
            Connection con;
        
            con = null;
        
            ResultSet rs = null;
        
            PreparedStatement pst = null;
        
            
            Class.forName("com.mysql.jdbc.Driver");
        
            
            con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/virus", "root", "Chiku@123");
        
            pst = (PreparedStatement) con.prepareStatement(sql);
        
            pst.setString(1, hash);
        
            rs = (ResultSet) pst.executeQuery();
        
            
            if ( rs.next())
            {
                JOptionPane.showMessageDialog(null, "File Contains Virus");        
            }
            
            else 
            {
                JOptionPane.showMessageDialog(null, "File is Free of Virus");
            }
        }
        catch (ClassNotFoundException | SQLException ex) 
        {
            Logger.getLogger(Virus_Scanner.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    
    Virus() {
        
        //throw new UnsupportedOperationException("Not supported yet."); 
        //To change body of generated methods, choose Tools | Templates.
    }
    
    void setVisible(boolean b) {
        
        //throw new UnsupportedOperationException("Not supported yet."); 
        //To change body of generated methods, choose Tools | Templates.
    }
        
}