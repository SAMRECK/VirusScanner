package virus_scanner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;



public class MD5
{
    String path, hash;
    
    private static final int   INIT_A     = 0x67452301;
    private static final int   INIT_B     = (int) 0xEFCDAB89L;
    private static final int   INIT_C     = (int) 0x98BADCFEL;
    private static final int   INIT_D     = 0x10325476;
    private static final int[] SHIFT_AMTS = { 7, 12, 17, 22, 5, 9, 14, 20, 4,
            11, 16, 23, 6, 10, 15, 21  };
    private static final int[] TABLE_T    = new int[64];
    
    ArrayList<byte[]> byTe = new ArrayList<>();
        
    public MD5(String filepath) throws IOException
    {
        path = filepath;
        
        
        System.out.println(path);
        
        if (path == null)
        {
            JOptionPane.showMessageDialog(null, "Please Select a File using BROWSE Button");
        }
        
        
        
        FileInputStream fis = null;
        
        try 
        {
            fis = new FileInputStream(path);    
            
        } 
            
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(Virus.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        byte[] dataByte = new byte[1024];
        int nread = 0;
        while ((nread = fis.read(dataByte))!= -1)
        {
            for (int i = 0; i< dataByte.length; i++)
            {
                byTe.add(dataByte);
            }
        }
        
        // Calling a function to compute MD5
        
        hash = toHex(md5Compute(byTe));
        System.out.println(hash + " <== \"" + path + "\"");
                
        // connection with database
        
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
          
    public static byte[] md5Compute(ArrayList message)
    {
        int messageLenBytes = message.size();
        
        int a = INIT_A;
        int b = INIT_B;
        int c = INIT_C;
        int d = INIT_D;
        int[] buffer;
        buffer = new int[16];
               
        for (int i = 0; i < messageLenBytes; i++)
        {
            int i_A = a;
            int i_B = b;
            int i_C = c;
            int i_D = d;
                      
            for (int j = 0; j < 64; j++)
            {
                int div16 = j >>> 4;
                int f = 0;
                int Index = j;
                switch (div16)
                {
                    case 0:
                        f = (b & c) | (~b & d);
                        break;
                    case 1:
                        f = (b & d) | (c & ~d);
                        Index = (Index * 5 + 1) & 0x0F;
                        break;
                    case 2:
                        f = b ^ c ^ d;
                        Index = (Index * 3 + 5) & 0x0F;
                        break;
                    case 3:
                        f = c ^ (b | ~d);
                        Index = (Index * 7) & 0x0F;
                        break;
                }
                int temp = b
                        + Integer.rotateLeft(a + f + buffer[Index]
                                + TABLE_T[j],
                                SHIFT_AMTS[(div16 << 2) | (j & 3)]);
                a = d;
                d = c;
                c = b;
                b = temp;
            }
            a += i_A;
            b += i_B;
            c += i_C;
            d += i_D;
        }
        byte[] md5 = new byte[16];
        int count = 0;
        for (int i = 0; i < 4; i++)
        {
            int n = (i == 0) ? a : ((i == 1) ? b : ((i == 2) ? c : d));
            for (int j = 0; j < 4; j++)
            {
                md5[count++] = (byte) n;
                n >>>= 8;
            }
        }
        return md5;
    }
 
    public static String toHex(byte[] b)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++)
        {
            sb.append(String.format("%02X", b[i] & 0xFF));
        }
        return sb.toString();
    }
    
    void setVisible(boolean b) {
        //throw new UnsupportedOperationException("Not supported yet.");
        //To change body of generated methods, choose Tools | Templates.
    }
    
    MD5() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}