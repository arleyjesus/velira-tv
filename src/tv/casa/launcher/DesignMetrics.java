package tv.casa.launcher;
final class DesignMetrics {
    static float scale(int width,int height){if(width<=0||height<=0)throw new IllegalArgumentException("Invalid display size");return Math.min(width/960f,height/540f);}
    static float inset(int pixels,int canvas,float scale){return (pixels-canvas*scale)/2;}
}
