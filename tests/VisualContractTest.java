package tv.casa.launcher;
public final class VisualContractTest {
    static void check(boolean value){if(!value)throw new AssertionError();}
    static double channel(int value){double c=value/255.0;return c<=.04045?c/12.92:Math.pow((c+.055)/1.055,2.4);}
    static double luminance(int rgb){return .2126*channel((rgb>>16)&255)+.7152*channel((rgb>>8)&255)+.0722*channel(rgb&255);}
    static double contrast(int a,int b){double x=luminance(a),y=luminance(b);return (Math.max(x,y)+.05)/(Math.min(x,y)+.05);}
    public static void main(String[] args){
        for(int background:new int[]{VisualTokens.CARBON,VisualTokens.GRAPHITE,VisualTokens.FOCUS_SURFACE})for(int foreground:new int[]{VisualTokens.INK,VisualTokens.MUTED,VisualTokens.ACCENT,VisualTokens.WARNING,VisualTokens.ERROR})check(contrast(background,foreground)>=4.5);
        for(int[] size:new int[][]{{1920,1080},{3840,2160},{1280,720},{1920,1200},{1024,768}}){float scale=DesignMetrics.scale(size[0],size[1]);check(scale>0);float x=DesignMetrics.inset(size[0],960,scale),y=DesignMetrics.inset(size[1],540,scale);check(x>=0&&y>=0);check(x+960*scale<=size[0]+.01&&y+540*scale<=size[1]+.01);check(y+(490+43)*scale<=size[1]);}
        check(DesignMetrics.scale(3840,2160)==2*DesignMetrics.scale(1920,1080));boolean invalid=false;try{DesignMetrics.scale(0,1080);}catch(IllegalArgumentException expected){invalid=true;}check(invalid);
        check(VisualTokens.focusMs(0)==0&&VisualTokens.focusScale(0)==1);for(int i=1;i<=3;i++){check(VisualTokens.focusMs(i)>=140&&VisualTokens.focusMs(i)<=200);check(VisualTokens.focusScale(i)<=1.065f);}
        System.out.println("Visual contract passed: text contrast on defined solid surfaces, bounded 1080p/4K design transform, disabled and enabled effect limits. This does not certify physical 4K rendering.");
    }
}
