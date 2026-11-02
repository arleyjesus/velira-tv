package tv.casa.launcher;

import android.app.Dialog;
import android.graphics.*;
import android.view.*;
import android.widget.*;

/** Three lightweight native diagrams, not screenshots or live wallpaper renderers. */
final class PresetPicker {
    static void show(MainActivity owner){
        owner.closePanel();Dialog dialog=new Dialog(owner);LinearLayout surface=new LinearLayout(owner);surface.setOrientation(1);surface.setPadding(owner.px(24),owner.px(20),owner.px(24),owner.px(20));surface.setBackground(AuraTheme.panel(owner,owner.px(VisualUi.CARD_RADIUS)));
        LinearLayout header=new LinearLayout(owner);header.setGravity(Gravity.CENTER_VERTICAL);header.addView(owner.text("Elige tu diseño",24,AuraTheme.INK),new LinearLayout.LayoutParams(0,owner.px(44),1));TextView close=owner.button("×",owner::closePanel);close.setContentDescription("Cerrar selector de diseños");header.addView(close,new LinearLayout.LayoutParams(owner.px(40),owner.px(40)));surface.addView(header);
        LinearLayout row=new LinearLayout(owner);row.setClipChildren(false);String[] keys={"classic","modern","immersive"},titles={"Clásico","Moderno","Inmersivo"},details={"Reloj y favoritas en una bandeja.","Bienvenida, reloj y aplicaciones.","Paisaje abierto e iconos flotantes."};View[] cards=new View[3];int selected=0;
        for(int i=0;i<3;i++){final String value=keys[i];LinearLayout card=new LinearLayout(owner);card.setOrientation(1);card.setPadding(owner.px(12),owner.px(12),owner.px(12),owner.px(10));owner.focusable(card);card.setBackground(AuraTheme.states(owner,owner.px(VisualUi.BUTTON_RADIUS),AuraTheme.ACCENT));card.setSelected(value.equals(owner.preset()));if(value.equals(owner.preset()))selected=i;card.setContentDescription(titles[i]+". "+details[i]+(value.equals(owner.preset())?" Diseño actual.":" OK para aplicar."));card.addView(new Miniature(owner,i),new LinearLayout.LayoutParams(-1,owner.px(112)));TextView title=owner.text(titles[i]+(value.equals(owner.preset())?" · Actual":""),16,AuraTheme.INK);card.addView(title,new LinearLayout.LayoutParams(-1,owner.px(34)));TextView detail=owner.text(details[i],12,AuraTheme.MUTED);detail.setMaxLines(2);card.addView(detail,new LinearLayout.LayoutParams(-1,owner.px(46)));card.setOnClickListener(v->{owner.getPreferences(0).edit().putString("preset",value).apply();owner.lastFocus="";owner.rebuildHome();});LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,owner.px(218),1);if(i>0)lp.leftMargin=owner.px(12);row.addView(card,lp);cards[i]=card;}
        LinearLayout.LayoutParams rp=new LinearLayout.LayoutParams(-1,-2);rp.topMargin=owner.px(16);surface.addView(row,rp);surface.addView(owner.text("Vistas esquemáticas · Flechas para elegir · OK para aplicar",11,AuraTheme.MUTED),new LinearLayout.LayoutParams(-1,owner.px(38)));
        for(int i=0;i<3;i++){cards[i].setNextFocusLeftId(cards[Math.max(0,i-1)].getId());cards[i].setNextFocusRightId(cards[Math.min(2,i+1)].getId());cards[i].setNextFocusUpId(close.getId());cards[i].setNextFocusDownId(cards[i].getId());}close.setNextFocusDownId(cards[selected].getId());
        dialog.setContentView(surface);VisualUi.window(dialog.getWindow(),owner,owner.px(828),WindowManager.LayoutParams.WRAP_CONTENT);dialog.setOnDismissListener(v->owner.panelDismissed(dialog));owner.activeDialog=dialog;dialog.show();cards[selected].requestFocus();
    }
    private static final class Miniature extends View {
        final Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);final int mode;
        Miniature(MainActivity owner,int mode){super(owner);this.mode=mode;setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);}
        void rect(Canvas canvas,float x,float y,float w,float h,int color,float radius){paint.setColor(color);canvas.drawRoundRect(x,y,x+w,y+h,radius,radius,paint);}
        protected void onDraw(Canvas canvas){super.onDraw(canvas);canvas.save();canvas.scale(getWidth()/160f,getHeight()/100f);rect(canvas,0,0,160,100,AuraTheme.CARBON,6);rect(canvas,1,49,158,50,0xFF263644,5);rect(canvas,8,8,31,3,AuraTheme.INK,1);rect(canvas,146,7,6,6,AuraTheme.ACCENT,3);
            if(mode==1){rect(canvas,8,23,89,37,AuraTheme.GRAPHITE,4);rect(canvas,102,23,50,37,AuraTheme.GRAPHITE,4);rect(canvas,15,30,64,4,AuraTheme.INK,1);rect(canvas,15,40,54,2,AuraTheme.MUTED,1);rect(canvas,15,49,33,5,AuraTheme.ACCENT,2);rect(canvas,111,33,32,12,AuraTheme.INK,2);}else{rect(canvas,10,28,38,3,AuraTheme.MUTED,1);rect(canvas,10,37,46,14,AuraTheme.INK,2);rect(canvas,10,56,50,2,AuraTheme.MUTED,1);}
            if(mode!=2)rect(canvas,6,66,148,21,AuraTheme.GRAPHITE,5);for(int i=0;i<6;i++)rect(canvas,mode==2?18+i*21:10+i*24,mode==2?69:70,mode==2?17:20,mode==2?17:13,i==0?AuraTheme.ACCENT:0xFF6F7C88,mode==2?5:3);rect(canvas,mode==2?39:25,92,mode==2?82:110,4,0xFF7D8C99,2);canvas.restore();}
    }
}
