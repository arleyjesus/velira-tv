package tv.casa.launcher;

/** One preference/backup contract shared by Appearance and the library toolbar. */
final class LibraryOptions {
    static int columns(MainActivity owner){int value=owner.getPreferences(0).getInt("libraryColumns",6);return DisplayOptions.valid("libraryColumns",value)?value:6;}
    static String label(int value){return value==4?"Grandes":value==5?"Equilibradas":"Compactas";}
    static android.app.AlertDialog show(MainActivity owner,Runnable changed){int[] values={4,5,6};String[] labels={"Grandes · 4 por fila","Equilibradas · 5 por fila","Compactas · 6 por fila"};return new VisualUi.Builder(owner).setTitle("Tarjetas de la biblioteca").setSingleChoiceItems(labels,columns(owner)-4,(dialog,which)->{owner.getPreferences(0).edit().putInt("libraryColumns",values[which]).apply();dialog.dismiss();changed.run();}).setNegativeButton("Volver",null).show();}
}
