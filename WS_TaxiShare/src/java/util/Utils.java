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
    public static List<Usuario> solveRecursionRotas(List<Usuario> recursive)
    {
        List<Usuario> notRecursive = new ArrayList();
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new TsExclusionStrategy(Rota.class))
                .setDateFormat("MMM/dd/yyyy")
                .serializeNulls()
                .create();
        String json = "";
        Type type = new TypeToken<List<Usuario>>(){}.getType();
        
        json = gson.toJson(recursive);
        notRecursive = gson.fromJson(json, type);
        return notRecursive;
    }
    
    public static Usuario solveRecursionRotas(Usuario recursive)
    {
        Usuario notRecursive = new Usuario();
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new TsExclusionStrategy(Rota.class))
                .serializeNulls()
                .setDateFormat("MMM-dd-yyyy")
                .create();
        String json = "";
        
        json = gson.toJson(recursive);
        notRecursive = gson.fromJson(json, Usuario.class);
        return notRecursive;
    }

}
