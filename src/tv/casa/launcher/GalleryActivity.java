package tv.casa.launcher;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import java.util.concurrent.*;

/** Legacy gallery: shared styling, cancellable work and no disk scan on the UI thread. */
public final class GalleryActivity extends Activity {
    private RemoteGallery gallery;private ExecutorService worker;private Future<?> task;
    private TextView status;private Button download,toggle,clear;private boolean visible,busy,loading;private int generation,localCount;private String focus="download";
    @Override public void onCreate(Bundle state){super.onCreate(state);gallery=new RemoteGallery(this);worker=Executors.newSingleThreadExecutor();getWindow().getDecorView().setSystemUiVisibility(5894);
        ScrollView scroll=new ScrollView(this);scroll.setFillViewport(true);scroll.setBackgroundColor(AuraTheme.CARBON);
        LinearLayout box=new LinearLayout(this);box.setOrientation(1);box.setPadding(VisualUi.px(this,70),VisualUi.px(this,24),VisualUi.px(this,70),VisualUi.px(this,24));scroll.addView(box);
        TextView title=new TextView(this);title.setText("Galería de fondos");VisualUi.text(title,VisualUi.TITLE,AuraTheme.INK);box.addView(title);
        TextView detail=new TextView(this);detail.setText("Solo se conecta a GitHub al descargar. Máximo 3 imágenes, 12 MiB por pack.\nGitHub recibe tu IP al conectarse. Las colecciones nuevas se administran desde Ambiente.");VisualUi.text(detail,VisualUi.BODY,AuraTheme.MUTED);box.addView(detail);
        status=new TextView(this);VisualUi.text(status,VisualUi.BODY,AuraTheme.MUTED);status.setPadding(0,VisualUi.px(this,12),0,VisualUi.px(this,12));status.setAccessibilityLiveRegion(View.ACCESSIBILITY_LIVE_REGION_POLITE);box.addView(status);
        download=button(box,"Descargar o actualizar desde GitHub","download",()->start(true));
        toggle=button(box,"Usar fondos descargados","toggle",()->{if(localCount==0){VisualUi.message(status,"Descarga un pack antes de activarlo.",true);return;}boolean enabled=Configuration.prefs(this).getBoolean("ambientRemote",false);Configuration.prefs(this).edit().putBoolean("ambientRemote",!enabled).apply();refresh();});
        clear=button(box,"Borrar fondos descargados","clear",()->start(false));
        button(box,"Ver ambiente","preview",()->{if(!busy)startActivity(new Intent(this,AmbientActivity.class));});button(box,"Volver","close",this::finish);setContentView(scroll);
    }
    private Button button(LinearLayout box,String label,String key,Runnable action){Button b=new Button(this);b.setText(label);b.setTag(key);b.setAllCaps(false);b.setMinHeight(0);b.setMinimumHeight(0);VisualUi.button(b);LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,VisualUi.px(this,VisualUi.ROW));lp.topMargin=VisualUi.px(this,VisualUi.GAP);box.addView(b,lp);b.setOnClickListener(v->{focus=key;action.run();});return b;}
    private void refresh(){download.setEnabled(!busy&&!loading);toggle.setEnabled(!busy&&!loading);clear.setEnabled(!busy&&!loading&&localCount>0);toggle.setText(Configuration.prefs(this).getBoolean("ambientRemote",false)?"Usar fondos integrados":"Usar fondos descargados");}
    private void load(){loading=true;refresh();VisualUi.message(status,"Consultando fondos locales…",false);int request=++generation;worker.execute(()->{int count=new RemoteGallery(getApplicationContext()).local().length;runOnUiThread(()->{if(!visible||request!=generation)return;localCount=count;loading=false;refresh();VisualUi.message(status,count==0?"Sin descargas · ambiente integrado disponible.":count+" fondos disponibles sin conexión.",false);View target=findViewById(android.R.id.content).findViewWithTag(focus);if(target!=null&&target.isEnabled())target.requestFocus();else download.requestFocus();});});}
    private void start(boolean fetch){if(busy||loading)return;busy=true;refresh();VisualUi.message(status,fetch?"Descargando y comprobando imágenes… Salir cancela la operación.":"Borrando descargas…",false);int request=++generation;RemoteGallery operation=new RemoteGallery(this);gallery=operation;task=worker.submit(()->{String message;boolean error=false;try{if(fetch){int n=operation.download();message=n+" fondos listos. Puedes activarlos.";}else{operation.clear();Configuration.prefs(this).edit().putBoolean("ambientRemote",false).commit();message="Descargas borradas. Se usan los fondos integrados.";}}catch(Exception e){error=true;message="Operación cancelada o incompleta. Se conservan los recursos válidos.";}int count=new RemoteGallery(getApplicationContext()).local().length;final String result=message;final boolean failed=error;runOnUiThread(()->{if(!visible||request!=generation)return;busy=false;localCount=count;refresh();VisualUi.message(status,result,failed);if(toggle.isEnabled())toggle.requestFocus();else download.requestFocus();});});}
    @Override public void onStart(){super.onStart();visible=true;busy=false;load();}
    @Override public void onStop(){visible=false;generation++;View v=getCurrentFocus();if(v!=null&&v.getTag() instanceof String)focus=(String)v.getTag();if(task!=null)task.cancel(true);gallery.cancel();busy=false;loading=false;super.onStop();}
    @Override public void onDestroy(){worker.shutdownNow();gallery.cancel();super.onDestroy();}
}
