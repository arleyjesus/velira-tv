package tv.casa.launcher;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/** Static translucent surfaces. No backdrop sampling, blur or animation loop. */
final class AuraTheme {
    static final int CARBON=0xFF151921, GRAPHITE=0xFF252C36, SLATE=0xFF515D69;
    static final int INK=0xFFF5F7FA, MUTED=0xFFB8C1CC, ACCENT=0xFF96C9DD, VIOLET=0xFF8375C9;
    static final int SUCCESS=0xFF9DEBB5, WARNING=0xFFE9C77D, ERROR=0xFFFFA5A5, DISABLED=0xFF7C8793, GAME=SUCCESS;
    static final int BUTTON_RADIUS=14, CARD_RADIUS=22, GAME_FOCUS_MS=160, CONTROLS_HIDE_MS=4000;
    static int focusMs(int level){return level==0?0:level==1?140:level==2?190:200;}
    static float focusScale(int level){return level==0?1:level==1?1.025f:level==2?1.05f:1.065f;}
    static int level(Context c){String value=Configuration.prefs(c).getString("visualEffects","low");return "off".equals(value)?0:"medium".equals(value)?2:"high".equals(value)?3:1;}
    static String label(String value){return "off".equals(value)?"Desactivados":"medium".equals(value)?"Medios":"high".equals(value)?"Altos":"Bajos";}
    static GradientDrawable surface(Context c,boolean focus,float radius,int accent){
        int level=level(c);
        int top=focus?0xEE515D69:0xDB252C36,bottom=focus?0xF0171B22:0xE8151921;
        if(level==0){top=bottom=focus?0xFF515D69:0xFF080B10;}
        else if(level>=2){top=focus?0xD9515D69:0xBD252C36;bottom=focus?0xED252C36:0xD9151921;}
        GradientDrawable g=new GradientDrawable(GradientDrawable.Orientation.TL_BR,new int[]{top,bottom});
        g.setCornerRadius(radius);g.setStroke(focus?Math.max(2,Math.round(radius/9)):Math.max(1,Math.round(radius/22)),focus?accent:level==3?0x9978B0C4:0x665A7183);return g;
    }
    static StateListDrawable states(Context c,float radius,int accent){StateListDrawable d=new StateListDrawable();GradientDrawable disabled=surface(c,false,radius,accent);disabled.setAlpha(100);d.addState(new int[]{-android.R.attr.state_enabled},disabled);d.addState(new int[]{android.R.attr.state_focused},surface(c,true,radius,accent));d.addState(new int[]{android.R.attr.state_pressed},surface(c,true,radius,accent));d.addState(new int[]{android.R.attr.state_selected},surface(c,false,radius,accent));d.addState(new int[]{},surface(c,false,radius,accent));return d;}
    static GradientDrawable dock(Context c,float radius){GradientDrawable g=new GradientDrawable(GradientDrawable.Orientation.TL_BR,new int[]{level(c)==0?0xFF292E36:0x70252C36,level(c)==0?0xFF171B22:0x88151921});g.setCornerRadius(radius);g.setStroke(Math.max(1,Math.round(radius/24)),0x668B98A4);return g;}
    static GradientDrawable panel(Context c,float radius){GradientDrawable g=new GradientDrawable(GradientDrawable.Orientation.TL_BR,new int[]{level(c)==0?0xFF080C12:0xF215202C,0xFC000000});g.setCornerRadius(radius);g.setStroke(1,0x667C9AAA);return g;}
}
