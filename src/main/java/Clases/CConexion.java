
package Clases;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;


public class CConexion {
    
    Connection conectar =null;
    
      String usuario = "root";
   String contrasenia = "1234";
   String bd = "bdusuarios";
   String ip = "localhost";
   String puerto = "3306";
   
   String cadena ="jdbc:mysql://"+ip+":"+puerto+"/"+bd;
   
   public Connection establecerConection(){
       
       try{
           Class.forName("com.mysql.cj.jdbc.Driver");
           conectar = DriverManager.getConnection(cadena,usuario,contrasenia);
           /*
           JOptionPane.showMessageDialog(null,"Conexion exitosa");
           */
           System.out.println("Conexion exitosa");
           
       }catch(Exception e){
           JOptionPane.showMessageDialog(null,"Error al conectar, "+e.toString());
       }
       return conectar;
   }
}

