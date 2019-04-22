/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virus_scanner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class Update {
    
    public String path;
    
    public Update(String filepath) throws NoSuchAlgorithmException
    {
        //System.out.println("Call");
        path = filepath;
        
        System.out.println(path);
    //}
    
        if (path == null)
        {
            JOptionPane.showMessageDialog(null, "Please Select a File using BROWSE Button");
        }
    
    
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    //public static void main(String[] args)throws Exception
    //{
        
        //System.out.println("Main Call");
        
        MessageDigest md = MessageDigest.getInstance("MD5");
        //System.out.println("14");
        FileInputStream fis = null;
        //System.out.println("15");
        try {
            fis = new FileInputStream(path);
            //System.out.println("16");
        } catch (FileNotFoundException ex) {
            //System.out.println("17");
            Logger.getLogger(Virus.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("18");
        }

        byte[] dataBytes = new byte[1024];
        //System.out.println("19");

        int nread = 0;
        try {
            //System.out.println("20");
            while ((nread = fis.read(dataBytes)) != -1)
            {
                //System.out.println("21");
                md.update(dataBytes, 0, nread);
                //System.out.println("22");
            }
        } catch (IOException ex) {
            Logger.getLogger(Virus.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] mdbytes = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) 
        {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //System.out.println("Digest(in hex format):: " + sb.toString());

        //String hash = "781770fda3bd3236d0ab8274577dddde";
        String hash = sb.toString();
        
        //System.out.println("Digest(in hex format):: " + hash);
        
        //System.out.println("Digest(in hex format):: " + hash1);
        
        String sql = "insert into hashing values(?);";
        
    }
}
