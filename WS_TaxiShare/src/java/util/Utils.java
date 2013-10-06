/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import TS.FrameWork.TO.Rota;
import TS.FrameWork.TO.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alan
 */
public class Utils {
    
    //tornar o metodo genérico para todas recursões utilizar o TsAdapter
    public static List<Rota> solveRecursion(List<Rota> recursive)
    {
        List<Rota> notRecursive = new ArrayList();
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new TsExclusionStrategy(Usuario.class))
                .serializeNulls()
                .create();
        String json = "";
        Type type = new TypeToken<List<Rota>>(){}.getType();
        
        json = gson.toJson(recursive);
        notRecursive = gson.fromJson(json, type);
        return notRecursive;
    }
    
}
