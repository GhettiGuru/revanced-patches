package app.revanced.extension.reddit.patches;

import com.reddit.domain.model.ILink;

import java.util.ArrayList;
import java.util.List;

import app.revanced.extension.reddit.settings.Settings;

@SuppressWarnings("unused")
public final class GeneralAdsPatch {

    private static List<?> filterChildren(final Iterable<?> links) {
        final List<Object> filteredList = new ArrayList<>();

        for (Object item : links) {
            if (item instanceof ILink iLink && iLink.getPromoted()) continue;

            filteredList.add(item);
        }

        return filteredList;
    }

    public static boolean hideCommentAds() {
        return Settings.HIDE_COMMENT_ADS.get();
    }

    public static List<?> hideOldPostAds(List<?> list) {
        if (!Settings.HIDE_OLD_POST_ADS.get())
            return list;

        return filterChildren(list);
    }

    public static void hideNewPostAds(ArrayList<Object> arrayList, Object object) {
        if (Settings.HIDE_NEW_POST_ADS.get())
            return;

        arrayList.add(object);
    }
}
