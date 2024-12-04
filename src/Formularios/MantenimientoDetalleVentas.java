/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Formularios;

import java.sql.CallableStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;

/**
 *
 * @author joser
 */
public class MantenimientoDetalleVentas {
    
    ConexionSQL cone = new ConexionSQL();
    
    public void mantenimientosventas(int codigo, int idProducto ,int cantidad,double preciounitario,String accion)
     {
         try{
             Connection con = cone.establecerConexion();
             CallableStatement cmd = con.prepareCall("{CALL sp_mantenimientoVentas(?,?,?,?,?,?,?)}");
         
             cmd.setInt(1,codigo);
             cmd.setInt(2,idProducto);
             cmd.setInt(3,cantidad);
             cmd.setDouble(4,preciounitario);
             cmd.setString(5,accion);
             
             cmd.execute();
         }catch(Exception ex){
             
             JOptionPane.showMessageDialog(null, ex.toString());}
     }
    
    
    
    
    
}
