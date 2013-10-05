/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import java.util.List;

/**
 *
 * @author alan
 */
public class TsExclusionStrategy implements ExclusionStrategy {
    
    private final Class<?> typeToSkip;

    public TsExclusionStrategy(Class<?> typeToSkip) {
      this.typeToSkip = typeToSkip;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
      
        return (clazz == typeToSkip);
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
      return false;
    }
    
}
