package main.java.utils;


import java.util.ArrayList;
import java.util.List;

public class LineBuilder {
    private static final Line LINE_VERTICAL = new Line(499, 289, 499, 599);
    private static final Line LINE_HORIZONTAL_1 = new Line(499, 599, 499 - 37, 599);
    private static final Line LINE_HORIZONTAL_2 = new Line(499, 599, 499 + 36, 599);

    private static final Line LINE_GOOGLE_1 = new Line(104, 259, 104, 289);
    private static final Line LINE_GOOGLE_2 = new Line(104, 289, 499, 289);

    private static final Line LINE_YANDEX_1 = new Line(499, 259, 499, 289);

    private static final Line LINE_STEAM_1 = new Line(894, 259, 894, 289);
    private static final Line LINE_STEAM_2 = new Line(894, 289, 499, 289);

    private static final Line LINE_BATTLENET_1 = new Line(274, 319, 274, 289);
    private static final Line LINE_BATTLENET_2 = new Line(274, 289, 499, 289);

    private static final Line LINE_CUSTOM_1 = new Line(684 + 40, 319, 684 + 40, 289);
    private static final Line LINE_CUSTOM_2 = new Line(684 + 40, 289, 499, 289);


    private static List<Line> getCommonLines() {
        return List.of(LINE_VERTICAL,
                LINE_HORIZONTAL_1,
                LINE_HORIZONTAL_2);
    }

    public static List<Line> getGoogleServerLines() {
        List<Line> googleLines = new ArrayList<>(getCommonLines());
        googleLines.addAll(List.of(
                LINE_GOOGLE_1,
                LINE_GOOGLE_2
        ));
        return googleLines;
    }

    public static List<Line> getYandexServerLines() {
        List<Line> yandexLines = new ArrayList<>(getCommonLines());
        yandexLines.add(LINE_YANDEX_1);
        return yandexLines;
    }

    public static List<Line> getSteamServerLines() {
        List<Line> steamLines = new ArrayList<>(getCommonLines());
        steamLines.addAll(List.of(
                LINE_STEAM_1,
                LINE_STEAM_2
        ));
        return steamLines;
    }

    public static List<Line> getBattlenetLines() {
        List<Line> battlenetLines = new ArrayList<>(getCommonLines());
        battlenetLines.addAll(List.of(
                LINE_BATTLENET_1,
                LINE_BATTLENET_2
        ));
        return battlenetLines;
    }

    public static List<Line> getCustomLines() {
        List<Line> customLines = new ArrayList<>(getCommonLines());
        customLines.addAll(List.of(
                LINE_CUSTOM_1,
                LINE_CUSTOM_2
        ));
        return customLines;
    }
}
