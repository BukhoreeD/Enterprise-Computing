/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mybean;

import javax.ejb.Stateless;

/**
 *
 * @author Bukhoree
 */
@Stateless
public class StatelessTemperatureConverterBean implements StatelessTemperatureConverterBeanRemote {

    @Override
    public float fToC(float temperature) {
    return (float) ((5.0 / 9.0) * (temperature - 32.0));
  }
}
