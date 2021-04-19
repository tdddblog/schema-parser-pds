package ek.pds.dd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import ek.util.CloseUtils;


public class DataTypeMap
{
    private Map<String, String> map;
    
    
    public DataTypeMap()
    {
        map = new HashMap<>();
    }

    
    public String getTargetType(String pdsType)
    {
        String targetType = map.get(pdsType);
        if(targetType != null) return targetType;
        
        targetType = guessType(pdsType);
        System.out.println("[WARN] No data type mapping for '" + pdsType + "'. Will use '" + targetType + "'");

        map.put(pdsType, targetType);
        return targetType;
    }
    
    
    private String guessType(String str)
    {
        str = str.toLowerCase();
        if(str.contains("_real")) return "double";
        if(str.contains("_integer")) return "integer";
        if(str.contains("_string")) return "string";
        if(str.contains("_text")) return "text";
        if(str.contains("_date")) return "date";
        if(str.contains("_boolean")) return "boolean";        
        
        return "string";
    }
    
    
    public void load(File file) throws Exception
    {
        System.out.println("Loading data type configuration from " + file.getAbsolutePath());
        
        BufferedReader rd = null;
        
        try
        {
            rd = new BufferedReader(new FileReader(file));
        }
        catch(Exception ex)
        {
            throw new Exception("Could not open data type configuration file '" + file.getAbsolutePath());
        }
        
        try
        {
            String line;
            while((line = rd.readLine()) != null)
            {
                line = line.trim();
                if(line.startsWith("#") || line.isEmpty()) continue;
                String[] tokens = line.split("=");
                if(tokens.length != 2) 
                {
                    throw new Exception("Invalid entry in data type configuration file " 
                            + file.getAbsolutePath() + ": " + line);
                }
                
                String key = tokens[0].trim();
                if(key.isEmpty()) 
                {
                    throw new Exception("Empty key in data type configuration file " 
                            + file.getAbsolutePath() + ": " + line);
                }
                
                String value = tokens[1].trim();
                if(key.isEmpty())
                {
                    throw new Exception("Empty value in data type configuration file " 
                            + file.getAbsolutePath() + ": " + line);
                }
                
                map.put(key, value);
            }
        }
        finally
        {
            CloseUtils.close(rd);
        }
    }
    
    
    public void debug()
    {
        for(String key: map.keySet())
        {
            String val = map.get(key);
            System.out.println(key + "  -->  " + val);
        }
    }
}
