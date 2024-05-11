
package Clases;

import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JComboBox;
import Clases.CConexion;
import com.toedter.calendar.JDateChooser;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.sql.CallableStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class CUsuario {
    int idSexo;

    public void establecerSexo(int idSexo) {
        this.idSexo = idSexo;
    }
    
    
    
    public void mostrarSexo(JComboBox combo){
        
        CConexion objetoConexion = new CConexion();
        
        String sql ="select * from sexo";
        
        Statement st;
        
        try{
            st = objetoConexion.establecerConection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            combo.removeAllItems();
            
            while(rs.next()){
                String nombreSexo = rs.getString("sexo");
                this.establecerSexo(rs.getInt("id"));
                
                combo.addItem(nombreSexo);
                combo.putClientProperty(nombreSexo, idSexo);
            }
           
            
        }catch(Exception e){
            e.printStackTrace();
            
        }
     
    }
    
    public void agregarUsuario(JTextField nombre,JTextField apellido,JComboBox sexo,JTextField edad,JDateChooser fecha,File foto){
        
        CConexion objetoConexion =new CConexion();
        
        String consultaSql = "insert into usuarios (nombres,apellidos,fksexo,edad,fnacimiento,foto) values(?,?,?,?,?,?);"; 
        
        try{
            FileInputStream file = new FileInputStream(foto);
            
            CallableStatement cs = objetoConexion.establecerConection().prepareCall(consultaSql);
            
            cs.setString(1, nombre.getText());
            cs.setString(2, apellido.getText());
            
            int idSexo = (int)sexo.getClientProperty(sexo.getSelectedItem());
            
            cs.setInt(3, idSexo);
            cs.setInt(4, Integer.parseInt(edad.getText()));
            
            Date fechaSelected = fecha.getDate();
            java.sql.Date fechasql = new java.sql.Date(fechaSelected.getTime());
            cs.setDate(5, fechasql);
            
            cs.setBinaryStream(6, file,(int)foto.length());
            cs.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    
    public void mostrarUsuarioes(JTable tablaUsuarios){
        
        CConexion objetoConexion = new CConexion();
        
        DefaultTableModel modelo = new DefaultTableModel();
        
        String sql="";
        
        modelo.addColumn("id");
        modelo.addColumn("Nombres");
        modelo.addColumn("Apellidos");
        modelo.addColumn("sexo");
        modelo.addColumn("Edad");
        modelo.addColumn("Nacimiento");
        modelo.addColumn("Foto");
        
        tablaUsuarios.setModel(modelo);
        
        sql= "select usuarios.id,usuarios.nombres,usuarios.apellidos,sexo.sexo,usuarios.edad,usuarios.fnacimiento,usuarios.foto from usuarios INNER JOIN sexo ON usuarios.fksexo=sexo.id;";
        
        try{
            Statement st = objetoConexion.establecerConection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                String id = rs.getString("id");
                String nombres = rs.getString("nombres");
                String apellidos= rs.getString("apellidos");
                String sexo= rs.getString("sexo");
                String edad = rs.getString("edad");
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                java.sql.Date fechasql = rs.getDate("fnacimiento");
                String nuevaFecha = sdf.format(fechasql);
                
                byte[]imageBytes = rs.getBytes("foto");
                Image foto = null;
                
                if(imageBytes !=null){
                    
                    try{
                        ImageIcon imageIcon = new ImageIcon(imageBytes);
                        foto = imageIcon.getImage();
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    modelo.addRow(new Object[]{id,nombres,apellidos,sexo,edad,nuevaFecha,foto});
                    }
                tablaUsuarios.setModel(modelo);
                }
            }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void seleccionarUsuarios(JTable usuarios,JTextField id,JTextField nombres,JTextField apellidos,JComboBox sexo, JTextField edad,JDateChooser fnacimiento,JLabel foto){
        
        int fila = usuarios.getSelectedRow();
        
        if(fila>=0){
            id.setText(usuarios.getValueAt(fila, 0).toString());
            nombres.setText(usuarios.getValueAt(fila, 1).toString());
            apellidos.setText(usuarios.getValueAt(fila,2 ).toString());
            
            sexo.setSelectedItem(usuarios.getValueAt(fila, 3).toString());
            edad.setText(usuarios.getValueAt(fila, 4).toString());
            String fechaString=usuarios.getValueAt(fila, 5).toString();
            
            Image img = (Image)usuarios.getValueAt(fila, 6);
            ImageIcon originalImg=new ImageIcon(img);
            int lblWidth = foto.getWidth();
            int lblHeigth = foto.getWidth();
            
            Image scaledImage = originalImg.getImage().getScaledInstance(lblWidth, lblHeigth,Image.SCALE_SMOOTH);
            foto.setIcon(new ImageIcon(scaledImage));
            
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date fechaDate = sdf.parse(fechaString);
                fnacimiento.setDate(fechaDate);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
    }
    
            public void modificarUsuario(JTextField nombre,JTextField apellido,JComboBox sexo,JTextField edad,JDateChooser fecha,File foto,JTextField id){
                CConexion objetoConexion= new CConexion();
                String sql = "UPDATE usuarios SET usuarios.nombres=?,usuarios.apellidos=?,usuarios.fksexo=?,usuarios.edad=?, usuarios.fnacimiento=? Where usuarios.id=? ";
                
                try{
                   
                     
                
                    CallableStatement cs = objetoConexion.establecerConection().prepareCall(sql);
                    
                    cs.setString(1, nombre.getText());
                    cs.setString(2, apellido.getText());
                    
                    int idSexo = (int)sexo.getClientProperty(sexo.getSelectedItem());
                    cs.setInt(3, idSexo);
                    cs.setInt(4,Integer.parseInt(edad.getText()));
                    
                    Date fechaSelected = fecha.getDate();
                    java.sql.Date fechaSql =new java.sql.Date(fechaSelected.getTime());
                    cs.setDate(5, fechaSql);
                 
                    cs.setInt(6,Integer.parseInt(id.getText()));
                    cs.execute();
                    
                    
                }catch(Exception e){
                    e.printStackTrace();
                    System.out.println(e.toString());
                }
                
            
        }
            
            public void eliminar(JTextField id){
                CConexion c = new CConexion();
                
                String consulta="DELETE FROM usuarios WHERE usuarios.id=?;";
                
                try{
                    CallableStatement cs = c.establecerConection().prepareCall(consulta);
                    cs.setInt(1, Integer.parseInt(id.getText()));
                    cs.execute();
                    System.out.println("Eliminado con exito");
                }catch(Exception e){
                    e.printStackTrace();
                }
                
                
                
                
            }
            
            
            public void limpiarCampos(JTextField id, JTextField nombre,JTextField apellido,JTextField edad, JDateChooser fnacimiento,JTextField rutaImagen, JLabel img){
                
                id.setText("");
                nombre.setText("");
                apellido.setText("");
                edad.setText("");
                
                Calendar calendario = Calendar.getInstance();
                fnacimiento.setDate(calendario.getTime());
                rutaImagen.setText(" ");
                img.setIcon(null);
            }

    
}