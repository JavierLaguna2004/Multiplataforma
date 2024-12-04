/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Formularios;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author joser
 */
public class MantenimientoCompras {
    ConexionSQL cone = new ConexionSQL();
    
     public DefaultComboBoxModel llenarproveedores(){
        
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        
        modelo.addElement("Seleccione: ");
        try{
            Connection con = cone.establecerConexion();
            CallableStatement cmd = con.prepareCall("{CALL sp_proveedores}");
            ResultSet rs = cmd.executeQuery();
             while(rs.next()){
                 int idProveedor = rs.getInt(1);
                String nombreProveedor = rs.getString(2);
                modelo.addElement(new ComboProveedor(idProveedor, nombreProveedor));
        }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,ex.toString());
        }
        return modelo;
    }
     
     public DefaultComboBoxModel llenarproductos(){
        
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        
        modelo.addElement(new ComboEmpleado(0, "Seleccione: ")); 
        try{
            Connection con = cone.establecerConexion();
            CallableStatement cmd = con.prepareCall("{CALL sp_productos}");
            ResultSet rs = cmd.executeQuery();
             while(rs.next()){
                  int idProducto = rs.getInt(1);
                String nombreProducto = rs.getString(2);
                modelo.addElement(new ComboProducto(idProducto, nombreProducto));
        }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,ex.toString());
        }
        return modelo;
    }
}
