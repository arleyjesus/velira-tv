package tv.casa.launcher;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;
import java.util.*;

/** Created on first use. GridView recycles cells; discovery uses the existing worker. */
final class AppLibraryView extends LinearLayout {
    private final MainActivity owner;
    private final GridView grid;
    private final TextView count;
    private final TextView retry;
    private final ArrayList<String> packages=new ArrayList<>();
    private final LibraryAdapter adapter=new LibraryAdapter();
    private int generation,columns;
    private final ArrayList<String> source=new ArrayList<>();
    private ArrayList<LibraryQuery.Entry> index=new ArrayList<>();
    private String query="";private boolean onlyFavorites,indexReady;
    private final TextView search,filter,density,reset,close;
    private android.app.AlertDialog editor,densityDialog;
    private final Map<String,Long> stableIds=new HashMap<>();
    private long nextId=1;

    AppLibraryView(MainActivity a){
        super(a);owner=a;setOrientation(VERTICAL);setPadding(a.px(24),a.px(15),a.px(24),a.px(12));
        GradientDrawable surface=AuraTheme.panel(a,a.px(23));setBackground(surface);
        LinearLayout header=new LinearLayout(a);header.setGravity(Gravity.CENTER_VERTICAL);
        TextView title=a.text("Todas las aplicaciones",24,MainActivity.INK);header.addView(title,new LayoutParams(0,a.px(40),1));
        close=a.button("×",a::closeLibrary);close.setContentDescription("Volver a Inicio");header.addView(close,new LayoutParams(a.px(40),a.px(36)));addView(header,new LayoutParams(-1,a.px(42)));
        LinearLayout toolbar=new LinearLayout(a);toolbar.setGravity(Gravity.CENTER_VERTICAL);
        search=a.button("Buscar",this::showSearch);filter=a.button("Todas",()->{onlyFavorites=!onlyFavorites;applyFilter();});density=a.button("Tarjetas",()->densityDialog=LibraryOptions.show(a,()->{applyDensity();applyFilter();}));reset=a.button("Limpiar",()->{query="";onlyFavorites=false;applyFilter();});
        TextView[] controls={search,filter,density,reset};for(int i=0;i<controls.length;i++){controls[i].setBackground(AuraTheme.states(a,a.px(VisualUi.BUTTON_RADIUS),AuraTheme.ACCENT));VisualUi.textState(controls[i]);controls[i].setMaxLines(1);controls[i].setEllipsize(android.text.TextUtils.TruncateAt.END);LayoutParams lp=new LayoutParams(i==0?0:a.px(i==1?145:i==2?155:85),a.px(36),i==0?1:0);if(i>0)lp.leftMargin=a.px(8);toolbar.addView(controls[i],lp);}LayoutParams toolbarParams=new LayoutParams(-1,a.px(42));toolbarParams.topMargin=a.px(4);addView(toolbar,toolbarParams);
        count=a.text("Arriba para volver a Inicio",11,MainActivity.MUTED);addView(count,new LayoutParams(-1,a.px(25)));
        count.setAccessibilityLiveRegion(View.ACCESSIBILITY_LIVE_REGION_POLITE);
        retry=a.button("Volver a cargar",this::reload);retry.setVisibility(GONE);addView(retry,new LayoutParams(-1,a.px(VisualUi.ROW)));
        grid=new GridView(a);columns=LibraryOptions.columns(a);grid.setNumColumns(columns);grid.setHorizontalSpacing(a.px(12));grid.setVerticalSpacing(a.px(10));grid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);grid.setClipToPadding(false);grid.setPadding(a.px(3),a.px(5),a.px(3),a.px(5));
        GradientDrawable outline=new GradientDrawable();outline.setColor(Color.TRANSPARENT);outline.setCornerRadius(a.px(15));outline.setStroke(a.px(2),AuraTheme.ACCENT);grid.setSelector(outline);grid.setDrawSelectorOnTop(true);grid.setAdapter(adapter);
        grid.setOnItemClickListener((parent,view,position,id)->{String pkg=packages.get(position);a.closeLibrary();a.launch(pkg);});
        grid.setOnItemLongClickListener((parent,view,position,id)->{a.closeLibrary();a.showApps(true);return true;});
        grid.setOnKeyListener((view,key,event)->{if(event.getAction()==KeyEvent.ACTION_DOWN&&key==KeyEvent.KEYCODE_DPAD_UP&&grid.getSelectedItemPosition()<columns){search.requestFocus();return true;}return false;});
        addView(grid,new LayoutParams(-1,0,1));wireToolbar();setVisibility(INVISIBLE);
    }

    void open(){
        setVisibility(VISIBLE);cancelAnimation();setAlpha(1);setTranslationY(0);
        applyDensity();if(owner.appCache!=null)prepare(owner.appCache);else reload();
        if(!packages.isEmpty()){grid.requestFocus();if(grid.getSelectedItemPosition()<0)grid.setSelection(0);}else getChildAt(0).requestFocus();
    }
    void hide(){if(editor!=null){editor.dismiss();editor=null;}if(densityDialog!=null){densityDialog.dismiss();densityDialog=null;}generation++;cancelAnimation();setVisibility(INVISIBLE);grid.clearFocus();}
    void cancelAnimation(){animate().cancel();}
    void invalidatePackages(){generation++;indexReady=false;if(getVisibility()==VISIBLE)reload();}
    private void reload(){
        final int request=++generation,packageRequest=owner.packageGeneration;count.setText("Cargando aplicaciones…");
        retry.setVisibility(GONE);
        owner.loader.execute(()->{
            ArrayList<String> found;try{found=owner.apps();}catch(Exception error){owner.handler.post(()->{if(!owner.alive||request!=generation)return;VisualUi.message(count,"No se pudieron cargar las aplicaciones. Puedes volver a intentarlo.",true);retry.setVisibility(VISIBLE);if(packages.isEmpty())retry.requestFocus();});return;}
            owner.handler.post(()->{if(!owner.alive||request!=generation||packageRequest!=owner.packageGeneration)return;owner.appCache=found;prepare(found);});
        });
    }
    private void wireToolbar(){TextView[] controls={search,filter,density,reset};for(int i=0;i<controls.length;i++){controls[i].setNextFocusLeftId(controls[Math.max(0,i-1)].getId());controls[i].setNextFocusRightId(controls[Math.min(controls.length-1,i+1)].getId());controls[i].setNextFocusUpId(close.getId());controls[i].setNextFocusDownId(grid.getId());}close.setNextFocusDownId(search.getId());grid.setId(View.generateViewId());for(TextView button:controls)button.setNextFocusDownId(grid.getId());}
    private void showSearch(){if(editor!=null&&editor.isShowing())return;EditText text=new EditText(owner);text.setTag("library-query");text.setSingleLine(true);text.setHint("Nombre de la aplicación");text.setInputType(android.text.InputType.TYPE_CLASS_TEXT);text.setFilters(new android.text.InputFilter[]{new android.text.InputFilter.LengthFilter(LibraryQuery.MAX_QUERY)});text.setText(query);text.selectAll();editor=new VisualUi.Builder(owner).setTitle("Buscar aplicaciones").setView(text).setPositiveButton("Buscar",(dialog,which)->{query=text.getText().toString().trim();applyFilter();}).setNeutralButton("Quitar búsqueda",(dialog,which)->{query="";applyFilter();}).setNegativeButton("Cancelar",null).show();}
    private void applyDensity(){columns=LibraryOptions.columns(owner);grid.setNumColumns(columns);density.setText("Tarjetas · "+LibraryOptions.label(columns));adapter.notifyDataSetChanged();}
    private void prepare(List<String> found){if(source.equals(found)&&indexReady){applyFilter();return;}indexReady=false;source.clear();source.addAll(found);int request=++generation;ArrayList<String> snapshot=new ArrayList<>(source);count.setText("Preparando biblioteca…");owner.loader.execute(()->{ArrayList<LibraryQuery.Entry> prepared=new ArrayList<>();for(String pkg:snapshot)prepared.add(new LibraryQuery.Entry(pkg,owner.label(pkg)));owner.handler.post(()->{if(!owner.alive||request!=generation)return;index=prepared;indexReady=true;applyFilter();});});}
    private void applyFilter(){search.setText(query.isEmpty()?"Buscar":"Buscar · "+query);filter.setText(onlyFavorites?"Favoritas":"Todas las apps");filter.setSelected(onlyFavorites);reset.setEnabled(!query.isEmpty()||onlyFavorites);density.setNextFocusRightId(reset.isEnabled()?reset.getId():density.getId());Set<String> favorites=new HashSet<>(Arrays.asList(owner.getPreferences(0).getString("favorites",String.join(",",MainActivity.DEFAULT_FAVORITES)).split(",")));display(LibraryQuery.filter(index,query,favorites,onlyFavorites));}
    private void display(List<String> found){int previous=grid.getSelectedItemPosition();String selected=previous>=0&&previous<packages.size()?packages.get(previous):null;if(!packages.equals(found)){packages.clear();packages.addAll(found);for(String pkg:found)if(!stableIds.containsKey(pkg))stableIds.put(pkg,nextId++);adapter.notifyDataSetChanged();int index=selected==null?-1:packages.indexOf(selected);if(!packages.isEmpty())grid.setSelection(index>=0?index:Math.max(0,Math.min(previous,packages.size()-1)));}retry.setVisibility(GONE);String summary=packages.isEmpty()?"Sin resultados. Cambia la búsqueda o el filtro; Atrás para volver.":packages.size()+" de "+source.size()+" aplicaciones   ·   ↑ filtros   ·   Mantén OK para editar favoritas";VisualUi.message(count,summary,false);if(getVisibility()==VISIBLE){if(packages.isEmpty())search.requestFocus();else grid.requestFocus();}}

    private final class LibraryAdapter extends BaseAdapter {
        public int getCount(){return packages.size();}
        public Object getItem(int position){return packages.get(position);}
        public boolean hasStableIds(){return true;}
        public long getItemId(int position){Long id=stableIds.get(packages.get(position));return id==null?0:id;}
        public View getView(int position,View recycled,android.view.ViewGroup parent){
            LinearLayout cell;
            if(recycled instanceof LinearLayout)cell=(LinearLayout)recycled;
            else{
                cell=new LinearLayout(owner);cell.setOrientation(VERTICAL);cell.setGravity(Gravity.CENTER);cell.setPadding(owner.px(7),owner.px(8),owner.px(7),owner.px(7));cell.setBackground(owner.surface(false));cell.setLayoutParams(new AbsListView.LayoutParams(-1,owner.px(88)));
                ImageView icon=new ImageView(owner);icon.setScaleType(ImageView.ScaleType.FIT_CENTER);cell.addView(icon,new LayoutParams(owner.px(46),owner.px(46)));
                TextView name=owner.text("",12,MainActivity.INK);name.setGravity(Gravity.CENTER);name.setMaxLines(1);name.setEllipsize(android.text.TextUtils.TruncateAt.END);LayoutParams np=new LayoutParams(-1,owner.px(23));np.topMargin=owner.px(3);cell.addView(name,np);
            }
            int height=columns==4?112:columns==5?100:88,edge=columns==4?64:columns==5?54:46;if(cell.getLayoutParams().height!=owner.px(height))cell.setLayoutParams(new AbsListView.LayoutParams(-1,owner.px(height)));ImageView artwork=(ImageView)cell.getChildAt(0);if(artwork.getLayoutParams().width!=owner.px(edge))artwork.setLayoutParams(new LayoutParams(owner.px(edge),owner.px(edge)));((TextView)cell.getChildAt(1)).setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,owner.px(columns==4?14:12));
            String pkg=packages.get(position);Assets.bindIcon(owner,(ImageView)cell.getChildAt(0),pkg);String name=owner.label(pkg);((TextView)cell.getChildAt(1)).setText(name);cell.setContentDescription(name);return cell;
        }
    }
}
