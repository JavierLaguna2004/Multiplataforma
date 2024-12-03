/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Formularios;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author carlo
 */
public class MantenimientoEmpleados {
      ConexionSQL conexion = new ConexionSQL();
      
       public void BuscarEmpleados(JTable tabla,String Dato,String accion){
    DefaultTableModel modelotabla= (DefaultTableModel) tabla.getModel();
    modelotabla.setRowCount(0);
  ResultSet rs;
    ResultSetMetaData rsmd;
    int columnas;
     try{
     
         Connection con =conexion.establecerConexion();
         CallableStatement cmd=con.prepareCall("{CALL BuscarEmpleadosPorIdentidad(?,?)}");
         cmd.setString(1, Dato);
         cmd.setString(2, accion);
           
         rs=cmd.executeQuery();
         rsmd=rs.getMetaData();
         columnas=rsmd.getColumnCount();
         while(rs.next()){
         
         Object[] fila= new Object [columnas];
         for(int i  =0;i<columnas;i++){
         
         fila[i]=rs.getObject(i+1);
         }
         modelotabla.addRow(fila);
         }

     
         
     }catch(Exception ex){
     
     JOptionPane.showMessageDialog(null, ex.toString());
     }
    
    }
    public void mantenimientoEmpleados(int codigo,String identidad,String Nombre,String Apellido,String Correo,String Direccion,Double Salario,String jornada,int idsucursal,int idpuesto,String accion){
    
     try{
     
         Connection con =conexion.establecerConexion();
         CallableStatement cmd=con.prepareCall("{CALL mantenimientoEmpleados(?,?,?,?,?,?,?,?,?,?,?)}");
         cmd.setInt(1, codigo);
         cmd.setString(2,identidad );
         cmd.setString(3, Nombre);
         cmd.setString(4, Apellido);
         cmd.setString(5, Correo);
         cmd.setString(6, Direccion);
         cmd.setDouble(7,Salario );
         cmd.setString(8,jornada );
         cmd.setInt(9,idsucursal );
         cmd.setInt(10,idpuesto);
         cmd.setString(11,accion);
        
         cmd.executeQuery();
         
     }catch(Exception ex){
     
     JOptionPane.showMessageDialog(null, ex.toString());
     }
    
    }
     public void cargartabla(JTable tabla,int codigo,String identidad,String Nombre,String Apellido,String Correo,String Direccion,Double Salario,String jornada,int idsucursal,int idpuesto,String accion){
    DefaultTableModel modelotabla= (DefaultTableModel) tabla.getModel();
    modelotabla.setRowCount(0);
  ResultSet rs;
    ResultSetMetaData rsmd;
    int columnas;
    
    
    try{
     
         Connection con =conexion.establecerConexion();
         CallableStatement cmd=con.prepareCall("{CALL mantenimientoEmpleados(?,?,?,?,?,?,?,?,?,?,?)}");
         cmd.setInt(1, codigo);
         cmd.setString(2,identidad );
         cmd.setString(3, Nombre);
         cmd.setString(4, Apellido);
         cmd.setString(5, Correo);
         cmd.setString(6, Direccion);
         cmd.setDouble(7,Salario );
         cmd.setString(8,jornada );
         cmd.setInt(9,idsucursal );
         cmd.setInt(10,idpuesto);
         cmd.setString(11,accion);
         
         rs=cmd.executeQuery();
         rsmd=rs.getMetaData();
         columnas=rsmd.getColumnCount();
         while(rs.next()){
         
         Object[] fila= new Object [columnas];
         for(int i  =0;i<columnas;i++){
         
         fila[i]=rs.getObject(i+1);
         }
         modelotabla.addRow(fila);
         }
            
        
    }catch(Exception ex){
     JOptionPane.showMessageDialog(null, ex.toString());
    }
    
    
    
    }
       public DefaultComboBoxModel llenarpuestos(){
    
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
    modelo.addElement("Seleccione");
    try{
        Connection con= conexion.establecerConexion();
        CallableStatement cmd=con.prepareCall("{CALL sp_puestos}");
        ResultSet rs= cmd.executeQuery();
        while (rs.next()){
        modelo.addElement(rs.getString(2));
        
        }
    
    }catch(Exception ex){
    
    JOptionPane.showMessageDialog(null, ex.toString());
    }
        return modelo;
    
    }
       public DefaultComboBoxModel llenarsucursales(){
    
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
    modelo.addElement("Seleccione");
    try{
        Connection con= conexion.establecerConexion();
        CallableStatement cmd=con.prepareCall("{CALL sp_sucursales}");
        ResultSet rs= cmd.executeQuery();
        while (rs.next()){
        modelo.addElement(rs.getString(2));
        
        }
    
    }catch(Exception ex){
    
    JOptionPane.showMessageDialog(null, ex.toString());
    }
        return modelo;
    
    }
       
       
}
