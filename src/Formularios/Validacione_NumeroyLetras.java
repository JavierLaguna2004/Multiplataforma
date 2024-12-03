/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Formularios;

/**
 *
 * @author carlo
 */
public class Validacione_NumeroyLetras {
    //jj
    
    public boolean ValidarNumeros(String dato){ 
  return dato.matches("[0-9]*");
    }
      public boolean ValidarLetras(String dato){ 
  return dato.matches("[a-z A-Z]*");
    }
    
    
    
    
}
