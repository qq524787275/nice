package com.zhuzichu.library.view.face.emoji;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.SparseIntArray;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.zhuzichu.library.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 用于作性能比较的控件。
 */
@SuppressWarnings({"JavaDoc", "UnusedReturnValue", "unused", "WeakerAccess"})
public final class EmojiconHandler {
    private static final HashMap<String, Integer> sQQFaceMap = new HashMap<>();
    private static final List<QQFace> mQQFaceList = new ArrayList<>();
    private static final SparseIntArray sSoftbanksMap = new SparseIntArray(125);
    private static final ArrayMap<String, String> mQQFaceFileNameList = new ArrayMap<>();//存储QQ表情对应的文件名,方便混淆后可以获取到原文件名
    /**
     * 表情的放大倍数
     */
    private static final float EMOJIICON_SCALE = 1.2f;
    /**
     * 表情的偏移值
     */
    private static final int EMOJIICON_TRANSLATE_Y = 0;
    private static final int QQFACE_TRANSLATE_Y = QMUIDisplayHelper.dpToPx(1);

    static {
        mQQFaceList.add(new QQFace("[可爱]", R.drawable.emoji_00));
        mQQFaceList.add(new QQFace("[大笑]", R.drawable.emoji_01));
        mQQFaceList.add(new QQFace("[色]", R.drawable.emoji_02));
        mQQFaceList.add(new QQFace("[嘘]", R.drawable.emoji_03));
        mQQFaceList.add(new QQFace("[亲]", R.drawable.emoji_04));
        mQQFaceList.add(new QQFace("[呆]", R.drawable.emoji_05));
        mQQFaceList.add(new QQFace("[口水]", R.drawable.emoji_06));
        mQQFaceList.add(new QQFace("[呲牙]", R.drawable.emoji_07));
        mQQFaceList.add(new QQFace("[鬼脸]", R.drawable.emoji_08));
        mQQFaceList.add(new QQFace("[害羞]", R.drawable.emoji_09));
        mQQFaceList.add(new QQFace("[偷笑]", R.drawable.emoji_10));
        mQQFaceList.add(new QQFace("[调皮]", R.drawable.emoji_11));
        mQQFaceList.add(new QQFace("[可怜]", R.drawable.emoji_12));
        mQQFaceList.add(new QQFace("[敲]", R.drawable.emoji_13));
        mQQFaceList.add(new QQFace("[惊讶]", R.drawable.emoji_14));
        mQQFaceList.add(new QQFace("[流感]", R.drawable.emoji_15));
        mQQFaceList.add(new QQFace("[委屈]", R.drawable.emoji_16));
        mQQFaceList.add(new QQFace("[流泪]", R.drawable.emoji_17));
        mQQFaceList.add(new QQFace("[嚎哭]", R.drawable.emoji_18));
        mQQFaceList.add(new QQFace("[惊恐]", R.drawable.emoji_19));
        mQQFaceList.add(new QQFace("[怒]", R.drawable.emoji_20));
        mQQFaceList.add(new QQFace("[酷]", R.drawable.emoji_21));
        mQQFaceList.add(new QQFace("[不说]", R.drawable.emoji_22));
        mQQFaceList.add(new QQFace("[鄙视]", R.drawable.emoji_23));
        mQQFaceList.add(new QQFace("[阿弥陀佛]", R.drawable.emoji_24));
        mQQFaceList.add(new QQFace("[奸笑]", R.drawable.emoji_25));
        mQQFaceList.add(new QQFace("[睡着]", R.drawable.emoji_26));
        mQQFaceList.add(new QQFace("[口罩]", R.drawable.emoji_27));
        mQQFaceList.add(new QQFace("[努力]", R.drawable.emoji_28));
        mQQFaceList.add(new QQFace("[抠鼻孔]", R.drawable.emoji_29));
        mQQFaceList.add(new QQFace("[疑问]", R.drawable.emoji_30));
        mQQFaceList.add(new QQFace("[怒骂]", R.drawable.emoji_31));
        mQQFaceList.add(new QQFace("[晕]", R.drawable.emoji_32));
        mQQFaceList.add(new QQFace("[呕吐]", R.drawable.emoji_33));
        mQQFaceList.add(new QQFace("[强]", R.drawable.emoji_34));
        mQQFaceList.add(new QQFace("[弱]", R.drawable.emoji_35));
        mQQFaceList.add(new QQFace("[OK]", R.drawable.emoji_36));
        mQQFaceList.add(new QQFace("[拳头]", R.drawable.emoji_37));
        mQQFaceList.add(new QQFace("[胜利]", R.drawable.emoji_38));
        mQQFaceList.add(new QQFace("[鼓掌]", R.drawable.emoji_39));
        mQQFaceList.add(new QQFace("[发怒]", R.drawable.emoji_40));
        mQQFaceList.add(new QQFace("[骷髅]", R.drawable.emoji_41));
        mQQFaceList.add(new QQFace("[便便]", R.drawable.emoji_42));
        mQQFaceList.add(new QQFace("[火]", R.drawable.emoji_43));
        mQQFaceList.add(new QQFace("[溜]", R.drawable.emoji_44));
        mQQFaceList.add(new QQFace("[爱心]", R.drawable.emoji_45));
        mQQFaceList.add(new QQFace("[心碎]", R.drawable.emoji_46));
        mQQFaceList.add(new QQFace("[钟情]", R.drawable.emoji_47));
        mQQFaceList.add(new QQFace("[唇]", R.drawable.emoji_48));
        mQQFaceList.add(new QQFace("[戒指]", R.drawable.emoji_49));
        mQQFaceList.add(new QQFace("[钻石]", R.drawable.emoji_50));
        mQQFaceList.add(new QQFace("[太阳]", R.drawable.emoji_51));
        mQQFaceList.add(new QQFace("[有时晴]", R.drawable.emoji_52));
        mQQFaceList.add(new QQFace("[多云]", R.drawable.emoji_53));
        mQQFaceList.add(new QQFace("[雷]", R.drawable.emoji_54));
        mQQFaceList.add(new QQFace("[雨]", R.drawable.emoji_55));
        mQQFaceList.add(new QQFace("[雪花]", R.drawable.emoji_56));
        mQQFaceList.add(new QQFace("[爱人]", R.drawable.emoji_57));
        mQQFaceList.add(new QQFace("[帽子]", R.drawable.emoji_58));
        mQQFaceList.add(new QQFace("[皇冠]", R.drawable.emoji_59));
        mQQFaceList.add(new QQFace("[篮球]", R.drawable.emoji_60));
        mQQFaceList.add(new QQFace("[足球]", R.drawable.emoji_61));
        mQQFaceList.add(new QQFace("[垒球]", R.drawable.emoji_62));
        mQQFaceList.add(new QQFace("[网球]", R.drawable.emoji_63));
        mQQFaceList.add(new QQFace("[台球]", R.drawable.emoji_64));
        mQQFaceList.add(new QQFace("[咖啡]", R.drawable.emoji_65));
        mQQFaceList.add(new QQFace("[啤酒]", R.drawable.emoji_66));
        mQQFaceList.add(new QQFace("[干杯]", R.drawable.emoji_67));
        mQQFaceList.add(new QQFace("[柠檬汁]", R.drawable.emoji_68));
        mQQFaceList.add(new QQFace("[餐具]", R.drawable.emoji_69));
        mQQFaceList.add(new QQFace("[汉堡]", R.drawable.emoji_70));
        mQQFaceList.add(new QQFace("[鸡腿]", R.drawable.emoji_71));
        mQQFaceList.add(new QQFace("[面条]", R.drawable.emoji_72));
        mQQFaceList.add(new QQFace("[冰淇淋]", R.drawable.emoji_73));
        mQQFaceList.add(new QQFace("[沙冰]", R.drawable.emoji_74));
        mQQFaceList.add(new QQFace("[生日蛋糕]", R.drawable.emoji_75));
        mQQFaceList.add(new QQFace("[蛋糕]", R.drawable.emoji_76));
        mQQFaceList.add(new QQFace("[糖果]", R.drawable.emoji_77));
        mQQFaceList.add(new QQFace("[葡萄]", R.drawable.emoji_78));
        mQQFaceList.add(new QQFace("[西瓜]", R.drawable.emoji_79));
        mQQFaceList.add(new QQFace("[光碟]", R.drawable.emoji_80));
        mQQFaceList.add(new QQFace("[手机]", R.drawable.emoji_81));
        mQQFaceList.add(new QQFace("[电话]", R.drawable.emoji_82));
        mQQFaceList.add(new QQFace("[电视]", R.drawable.emoji_83));
        mQQFaceList.add(new QQFace("[声音开启]", R.drawable.emoji_84));
        mQQFaceList.add(new QQFace("[声音关闭]", R.drawable.emoji_85));
        mQQFaceList.add(new QQFace("[铃铛]", R.drawable.emoji_86));
        mQQFaceList.add(new QQFace("[锁头]", R.drawable.emoji_87));
        mQQFaceList.add(new QQFace("[放大镜]", R.drawable.emoji_88));
        mQQFaceList.add(new QQFace("[灯泡]", R.drawable.emoji_89));
        mQQFaceList.add(new QQFace("[锤头]", R.drawable.emoji_90));
        mQQFaceList.add(new QQFace("[烟]", R.drawable.emoji_91));
        mQQFaceList.add(new QQFace("[炸弹]", R.drawable.emoji_92));
        mQQFaceList.add(new QQFace("[枪]", R.drawable.emoji_93));
        mQQFaceList.add(new QQFace("[刀]", R.drawable.emoji_94));
        mQQFaceList.add(new QQFace("[药]", R.drawable.emoji_95));
        mQQFaceList.add(new QQFace("[打针]", R.drawable.emoji_96));
        mQQFaceList.add(new QQFace("[钱袋]", R.drawable.emoji_97));
        mQQFaceList.add(new QQFace("[钞票]", R.drawable.emoji_98));
        mQQFaceList.add(new QQFace("[银行卡]", R.drawable.emoji_99));
        mQQFaceList.add(new QQFace("[手柄]", R.drawable.emoji_100));
        mQQFaceList.add(new QQFace("[麻将]", R.drawable.emoji_101));
        mQQFaceList.add(new QQFace("[调色板]", R.drawable.emoji_102));
        mQQFaceList.add(new QQFace("[电影]", R.drawable.emoji_103));
        mQQFaceList.add(new QQFace("[麦克风]", R.drawable.emoji_104));
        mQQFaceList.add(new QQFace("[耳机]", R.drawable.emoji_105));
        mQQFaceList.add(new QQFace("[音乐]", R.drawable.emoji_106));
        mQQFaceList.add(new QQFace("[吉他]", R.drawable.emoji_107));
        mQQFaceList.add(new QQFace("[火箭]", R.drawable.emoji_108));
        mQQFaceList.add(new QQFace("[飞机]", R.drawable.emoji_109));
        mQQFaceList.add(new QQFace("[火车]", R.drawable.emoji_110));
        mQQFaceList.add(new QQFace("[公交]", R.drawable.emoji_111));
        mQQFaceList.add(new QQFace("[轿车]", R.drawable.emoji_112));
        mQQFaceList.add(new QQFace("[出租车]", R.drawable.emoji_113));
        mQQFaceList.add(new QQFace("[警车]", R.drawable.emoji_114));
        mQQFaceList.add(new QQFace("[自行车]", R.drawable.emoji_115));
        mQQFaceList.add(new QQFace("[汗]", R.drawable.emoji_145));
        mQQFaceList.add(new QQFace("[拜一拜]", R.drawable.emoji_160));
        mQQFaceList.add(new QQFace("[惊喜]", R.drawable.emoji_161));
        mQQFaceList.add(new QQFace("[流汗]", R.drawable.emoji_162));
        mQQFaceList.add(new QQFace("[卖萌]", R.drawable.emoji_163));
        mQQFaceList.add(new QQFace("[默契眨眼]", R.drawable.emoji_164));
        mQQFaceList.add(new QQFace("[烧香拜佛]", R.drawable.emoji_165));
        mQQFaceList.add(new QQFace("[晚安]", R.drawable.emoji_166));
        mQQFaceList.add(new QQFace("[握手]", R.drawable.emoji_167));

        for (QQFace face : mQQFaceList) {
            sQQFaceMap.put(face.getName(), face.getRes());
        }

        mQQFaceFileNameList.put("[可爱]", "emoji_00");
        mQQFaceFileNameList.put("[大笑]", "emoji_01");
        mQQFaceFileNameList.put("[色]", "emoji_02");
        mQQFaceFileNameList.put("[嘘]", "emoji_03");
        mQQFaceFileNameList.put("[亲]", "emoji_04");
        mQQFaceFileNameList.put("[呆]", "emoji_05");
        mQQFaceFileNameList.put("[口水]", "emoji_06");
        mQQFaceFileNameList.put("[呲牙]", "emoji_07");
        mQQFaceFileNameList.put("[鬼脸]", "emoji_08");
        mQQFaceFileNameList.put("[害羞]", "emoji_09");
        mQQFaceFileNameList.put("[偷笑]", "emoji_10");
        mQQFaceFileNameList.put("[调皮]", "emoji_11");
        mQQFaceFileNameList.put("[可怜]", "emoji_12");
        mQQFaceFileNameList.put("[敲]", "emoji_13");
        mQQFaceFileNameList.put("[惊讶]", "emoji_14");
        mQQFaceFileNameList.put("[流感]", "emoji_15");
        mQQFaceFileNameList.put("[委屈]", "emoji_16");
        mQQFaceFileNameList.put("[流泪]", "emoji_17");
        mQQFaceFileNameList.put("[嚎哭]", "emoji_18");
        mQQFaceFileNameList.put("[惊恐]", "emoji_19");
        mQQFaceFileNameList.put("[怒]", "emoji_20");
        mQQFaceFileNameList.put("[酷]", "emoji_21");
        mQQFaceFileNameList.put("[不说]", "emoji_22");
        mQQFaceFileNameList.put("[鄙视]", "emoji_23");
        mQQFaceFileNameList.put("[阿弥陀佛]", "emoji_24");
        mQQFaceFileNameList.put("[奸笑]", "emoji_25");
        mQQFaceFileNameList.put("[睡着]", "emoji_26");
        mQQFaceFileNameList.put("[口罩]", "emoji_27");
        mQQFaceFileNameList.put("[努力]", "emoji_28");
        mQQFaceFileNameList.put("[抠鼻孔]", "emoji_29");
        mQQFaceFileNameList.put("[疑问]", "emoji_30");
        mQQFaceFileNameList.put("[怒骂]", "emoji_31");
        mQQFaceFileNameList.put("[晕]", "emoji_32");
        mQQFaceFileNameList.put("[呕吐]", "emoji_33");
        mQQFaceFileNameList.put("[强]", "emoji_34");
        mQQFaceFileNameList.put("[弱]", "emoji_35");
        mQQFaceFileNameList.put("[OK]", "emoji_36");
        mQQFaceFileNameList.put("[拳头]", "emoji_37");
        mQQFaceFileNameList.put("[胜利]", "emoji_38");
        mQQFaceFileNameList.put("[鼓掌]", "emoji_39");
        mQQFaceFileNameList.put("[发怒]", "emoji_40");
        mQQFaceFileNameList.put("[骷髅]", "emoji_41");
        mQQFaceFileNameList.put("[便便]", "emoji_42");
        mQQFaceFileNameList.put("[火]", "emoji_43");
        mQQFaceFileNameList.put("[溜]", "emoji_44");
        mQQFaceFileNameList.put("[爱心]", "emoji_45");
        mQQFaceFileNameList.put("[心碎]", "emoji_46");
        mQQFaceFileNameList.put("[钟情]", "emoji_47");
        mQQFaceFileNameList.put("[唇]", "emoji_48");
        mQQFaceFileNameList.put("[戒指]", "emoji_49");
        mQQFaceFileNameList.put("[钻石]", "emoji_50");
        mQQFaceFileNameList.put("[太阳]", "emoji_51");
        mQQFaceFileNameList.put("[有时晴]", "emoji_52");
        mQQFaceFileNameList.put("[多云]", "emoji_53");
        mQQFaceFileNameList.put("[雷]", "emoji_54");
        mQQFaceFileNameList.put("[雨]", "emoji_55");
        mQQFaceFileNameList.put("[雪花]", "emoji_56");
        mQQFaceFileNameList.put("[爱人]", "emoji_57");
        mQQFaceFileNameList.put("[帽子]", "emoji_58");
        mQQFaceFileNameList.put("[皇冠]", "emoji_59");
        mQQFaceFileNameList.put("[篮球]", "emoji_60");
        mQQFaceFileNameList.put("[足球]", "emoji_61");
        mQQFaceFileNameList.put("[垒球]", "emoji_62");
        mQQFaceFileNameList.put("[网球]", "emoji_63");
        mQQFaceFileNameList.put("[台球]", "emoji_64");
        mQQFaceFileNameList.put("[咖啡]", "emoji_65");
        mQQFaceFileNameList.put("[啤酒]", "emoji_66");
        mQQFaceFileNameList.put("[干杯]", "emoji_67");
        mQQFaceFileNameList.put("[柠檬汁]", "emoji_68");
        mQQFaceFileNameList.put("[餐具]", "emoji_69");
        mQQFaceFileNameList.put("[汉堡]", "emoji_70");
        mQQFaceFileNameList.put("[鸡腿]", "emoji_71");
        mQQFaceFileNameList.put("[面条]", "emoji_72");
        mQQFaceFileNameList.put("[冰淇淋]", "emoji_73");
        mQQFaceFileNameList.put("[沙冰]", "emoji_74");
        mQQFaceFileNameList.put("[生日蛋糕]", "emoji_75");
        mQQFaceFileNameList.put("[蛋糕]", "emoji_76");
        mQQFaceFileNameList.put("[糖果]", "emoji_77");
        mQQFaceFileNameList.put("[葡萄]", "emoji_78");
        mQQFaceFileNameList.put("[西瓜]", "emoji_79");
        mQQFaceFileNameList.put("[光碟]", "emoji_80");
        mQQFaceFileNameList.put("[手机]", "emoji_81");
        mQQFaceFileNameList.put("[电话]", "emoji_82");
        mQQFaceFileNameList.put("[电视]", "emoji_83");
        mQQFaceFileNameList.put("[声音开启]", "emoji_84");
        mQQFaceFileNameList.put("[声音关闭]", "emoji_85");
        mQQFaceFileNameList.put("[铃铛]", "emoji_86");
        mQQFaceFileNameList.put("[锁头]", "emoji_87");
        mQQFaceFileNameList.put("[放大镜]", "emoji_88");
        mQQFaceFileNameList.put("[灯泡]", "emoji_89");
        mQQFaceFileNameList.put("[锤头]", "emoji_90");
        mQQFaceFileNameList.put("[烟]", "emoji_91");
        mQQFaceFileNameList.put("[炸弹]", "emoji_92");
        mQQFaceFileNameList.put("[枪]", "emoji_93");
        mQQFaceFileNameList.put("[刀]", "emoji_94");
        mQQFaceFileNameList.put("[药]", "emoji_95");
        mQQFaceFileNameList.put("[打针]", "emoji_96");
        mQQFaceFileNameList.put("[钱袋]", "emoji_97");
        mQQFaceFileNameList.put("[钞票]", "emoji_98");
        mQQFaceFileNameList.put("[银行卡]", "emoji_99");
        mQQFaceFileNameList.put("[手柄]", "emoji_100");
        mQQFaceFileNameList.put("[麻将]", "emoji_101");
        mQQFaceFileNameList.put("[调色板]", "emoji_102");
        mQQFaceFileNameList.put("[电影]", "emoji_103");
        mQQFaceFileNameList.put("[麦克风]", "emoji_104");
        mQQFaceFileNameList.put("[耳机]", "emoji_105");
        mQQFaceFileNameList.put("[音乐]", "emoji_106");
        mQQFaceFileNameList.put("[吉他]", "emoji_107");
        mQQFaceFileNameList.put("[火箭]", "emoji_108");
        mQQFaceFileNameList.put("[飞机]", "emoji_109");
        mQQFaceFileNameList.put("[火车]", "emoji_110");
        mQQFaceFileNameList.put("[公交]", "emoji_111");
        mQQFaceFileNameList.put("[轿车]", "emoji_112");
        mQQFaceFileNameList.put("[出租车]", "emoji_113");
        mQQFaceFileNameList.put("[警车]", "emoji_114");
        mQQFaceFileNameList.put("[自行车]", "emoji_115");
        mQQFaceFileNameList.put("[汗]", "emoji_145");
        mQQFaceFileNameList.put("[拜一拜]", "emoji_160");
        mQQFaceFileNameList.put("[惊喜]", "emoji_161");
        mQQFaceFileNameList.put("[流汗]", "emoji_162");
        mQQFaceFileNameList.put("[卖萌]", "emoji_163");
        mQQFaceFileNameList.put("[默契眨眼]", "emoji_164");
        mQQFaceFileNameList.put("[烧香拜佛]", "emoji_165");
        mQQFaceFileNameList.put("[晚安]", "emoji_166");
        mQQFaceFileNameList.put("[握手]", "emoji_167");
    }
    private EmojiconHandler() {
    }

    private static boolean isSoftBankEmoji(char c) {
        return ((c >> 12) == 0xe);
    }



    private static int getSoftbankEmojiResource(char c) {
        return sSoftbanksMap.get(c);
    }

    /**
     * @param context   Convert emoji characters of the given Spannable to the according emojicon.
     * @param text
     * @param emojiSize
     */
    public static void addEmojis(Context context, SpannableStringBuilder text, int emojiSize, int textSize) {
        addEmojis(context, text, emojiSize, textSize, 0, -1, false);
    }

    /**
     * Convert emoji characters of the given Spannable to the according emojicon.
     *
     * @param context
     * @param text
     * @param emojiSize
     * @param index
     * @param length
     */
    public static void addEmojis(Context context, SpannableStringBuilder text, int emojiSize, int textSize, int index, int length) {
        addEmojis(context, text, emojiSize, textSize, index, length, false);
    }

    /**
     * Convert emoji characters of the given Spannable to the according emojicon.
     *
     * @param context
     * @param text
     * @param emojiSize
     * @param useSystemDefault
     */
    public static void addEmojis(Context context, SpannableStringBuilder text, int emojiSize, int textSize, boolean useSystemDefault) {
        addEmojis(context, text, emojiSize, textSize, 0, -1, useSystemDefault);
    }

    /**
     * Convert emoji characters of the given Spannable to the according emojicon.
     *
     * @param context
     * @param text
     * @param emojiSize
     * @param index
     * @param length
     * @param useSystemDefault
     */
    public static SpannableStringBuilder addEmojis(Context context, SpannableStringBuilder text, int emojiSize, int textSize, int index, int length, boolean useSystemDefault) {
        if (useSystemDefault) {
            return text;
        }

        int textLengthToProcess = calculateLegalTextLength(text, index, length);

        // remove spans throughout all text
        EmojiconSpan[] oldSpans = text.getSpans(0, text.length(), EmojiconSpan.class);
        for (EmojiconSpan oldSpan : oldSpans) {
            text.removeSpan(oldSpan);
        }

        int[] results = new int[3];
        String textStr = text.toString();

        int processIdx = index;
        while (processIdx < textLengthToProcess) {
            int skip = results[1];
            processIdx += skip;
        }
        return (SpannableStringBuilder) text.subSequence(index, processIdx);
    }



    public static String findQQFaceFileName(String key) {
        return mQQFaceFileNameList.get(key);
    }

    private static int calculateLegalTextLength(SpannableStringBuilder text, int index, int length) {
        int textLength = text.length();
        int textLengthToProcessMax = textLength - index;
        return (length < 0 || length >= textLengthToProcessMax ? textLength : (length + index));
    }

    public static List<QQFace> getQQFaceKeyList() {
        return mQQFaceList;
    }

    public static boolean isQQFaceCodeExist(String qqFaceCode) {
        return sQQFaceMap.get(qqFaceCode) != null;
    }

    public static class QQFace {
        private String name;
        private int res;

        public QQFace(String name, int res) {
            this.name = name;
            this.res = res;
        }

        public String getName() {
            return name;
        }

        public int getRes() {
            return res;
        }
    }
}
