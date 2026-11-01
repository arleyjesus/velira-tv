package tv.casa.launcher;

/** Platform-independent visual contract, shared by native UI and JVM checks. */
class VisualTokens {
    static final int CARBON=0xFF151921, GRAPHITE=0xFF252C36, SLATE=0xFF515D69, FOCUS_SURFACE=0xFF3A4652;
    static final int INK=0xFFF5F7FA, MUTED=0xFFB8C1CC, ACCENT=0xFF96C9DD, VIOLET=0xFF8375C9;
    static final int SUCCESS=0xFF9DEBB5, WARNING=0xFFE9C77D, ERROR=0xFFFFA5A5, DISABLED=0xFF7C8793, GAME=SUCCESS;
    static final int BUTTON_RADIUS=14, CARD_RADIUS=22, GAME_FOCUS_MS=160, CONTROLS_HIDE_MS=4000;
    static int focusMs(int level){return level==0?0:level==1?140:level==2?190:200;}
    static float focusScale(int level){return level==0?1:level==1?1.025f:level==2?1.05f:1.065f;}
    static final int TITLE=30, DIALOG_TITLE=24, SECTION=17, LABEL=16, BODY=14, SECONDARY=12, CAPTION=11;
    static final int GAP=8, SPACE=12, INSET=24, ROW=46;
}
