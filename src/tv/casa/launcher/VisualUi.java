package tv.casa.launcher;

import android.app.*;
import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;

/** Shared native components. Coordinates remain in the 960 x 540 design canvas. */
final class VisualUi extends VisualTokens {
    static float scale(Context context){DisplayMetrics d=context.getResources().getDisplayMetrics();return DesignMetrics.scale(d.widthPixels,d.heightPixels);}
    static int px(Context context,float value){return Math.round(value*scale(context));}
    static void text(TextView view,int size,int color){view.setTextColor(color);view.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,px(view.getContext(),size));view.setIncludeFontPadding(false);view.setTypeface(Typeface.create(size>=DIALOG_TITLE?"sans-serif-medium":"sans-serif",0));}
    static void button(TextView view){Context c=view.getContext();text(view,BODY,AuraTheme.INK);view.setTextColor(new android.content.res.ColorStateList(new int[][]{new int[]{-android.R.attr.state_enabled},new int[]{}},new int[]{AuraTheme.DISABLED,AuraTheme.INK}));view.setGravity(Gravity.CENTER);view.setPadding(px(c,14),px(c,8),px(c,14),px(c,8));view.setBackground(AuraTheme.states(c,px(c,BUTTON_RADIUS),AuraTheme.ACCENT));view.setFocusable(true);view.setFocusableInTouchMode(true);if(view.getId()==View.NO_ID)view.setId(View.generateViewId());}
    static void message(TextView view,String message,boolean error){view.setText(message);view.setTextColor(error?AuraTheme.ERROR:AuraTheme.MUTED);view.setAccessibilityLiveRegion(View.ACCESSIBILITY_LIVE_REGION_POLITE);}
    static void window(Window window,Context c,int width,int height){WindowManager.LayoutParams p=window.getAttributes();p.width=width;p.height=height;p.dimAmount=.48f;window.setAttributes(p);window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);window.setGravity(Gravity.CENTER);window.setBackgroundDrawableResource(android.R.color.transparent);window.getDecorView().setSystemUiVisibility(5894);if(AuraTheme.level(c)==0)window.setWindowAnimations(0);}

    /** Styles framework dialogs without replacing their list, IME or accessibility behavior. */
    static final class Builder extends AlertDialog.Builder {
        final Activity owner;
        Builder(Activity owner){super(owner);this.owner=owner;}
        @Override public AlertDialog create(){AlertDialog dialog=super.create();View anchor=owner.getCurrentFocus();Window w=dialog.getWindow();if(w!=null){VisualUi.window(w,owner,px(owner,610),WindowManager.LayoutParams.WRAP_CONTENT);w.setBackgroundDrawable(AuraTheme.panel(owner,px(owner,CARD_RADIUS)));}
            dialog.setOnShowListener(ignored->{for(int id:new int[]{AlertDialog.BUTTON_POSITIVE,AlertDialog.BUTTON_NEGATIVE,AlertDialog.BUTTON_NEUTRAL}){Button button=dialog.getButton(id);if(button!=null){button.setAllCaps(false);VisualUi.button(button);}}ListView list=dialog.getListView();if(list!=null){list.setSelector(AuraTheme.states(owner,px(owner,BUTTON_RADIUS),AuraTheme.ACCENT));list.setDrawSelectorOnTop(false);}View message=dialog.findViewById(android.R.id.message);if(message instanceof TextView)VisualUi.text((TextView)message,BODY,AuraTheme.INK);});
            dialog.setOnDismissListener(ignored->{if(anchor!=null)anchor.post(()->{if(!owner.isFinishing()&&anchor.isAttachedToWindow()&&anchor.isShown()&&anchor.isEnabled()&&owner.hasWindowFocus())anchor.requestFocus();});});return dialog;
        }
    }
}
