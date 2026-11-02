package tv.casa.launcher;

import java.text.Normalizer;
import java.util.*;

/** Immutable, locally built search index. No PackageManager or disk work during filtering. */
final class LibraryQuery {
    static final int MAX_QUERY=80;
    static String fold(String value){return Normalizer.normalize(value,Normalizer.Form.NFD).replaceAll("\\p{M}+","").toLowerCase(Locale.ROOT).trim();}
    static final class Entry {
        final String pkg,key;
        Entry(String pkg,String label){this.pkg=pkg;key=fold(label)+" "+fold(pkg);}
    }
    static ArrayList<String> filter(List<Entry> source,String query,Set<String> favorites,boolean onlyFavorites){String key=fold(query.length()>MAX_QUERY?query.substring(0,MAX_QUERY):query);ArrayList<String> result=new ArrayList<>();for(Entry entry:source)if((!onlyFavorites||favorites.contains(entry.pkg))&&entry.key.contains(key))result.add(entry.pkg);return result;}
}
