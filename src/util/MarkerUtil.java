package util;

import action.VersionThreeAction;
import com.intellij.openapi.editor.Editor;
import marker.Marker;
import marker.MarkerComparator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by runed on 11/12/2016.
 */
public class MarkerUtil {
    private static final String MARKER_SET = "AZWSXEDCFVTGBYHNUJMIKOLPÆÅØ";

    public static List<Marker> offsetsToMarkers(List<Integer> offsets, String searchString){
        return offsets.stream()
                .map(offset -> {
                    Marker temp = new Marker();
                    temp.setStartOffset(offset);
                    temp.setEndOffset(offset);
                    temp.setMarkerText(searchString);
                    temp.setReplacementText("");
                    return temp;
                })
                .collect(Collectors.toList());
    }

    public static List<Marker> sortMarkersAndAssignReplacementText(List<Marker> markers, int contextPoint) {
        markers.sort((o1, o2) -> {
            int o1ToCaret = Math.abs(o1.getStartOffset() - contextPoint);
            int o2ToCaret = Math.abs(o2.getStartOffset() - contextPoint);
            return o1ToCaret - o2ToCaret;
        });

        char[] markerSetChars = MARKER_SET.toCharArray();
        for(int i = 0; i < markers.size(); i++){
            Marker marker = markers.get(i);
            if(i < MARKER_SET.length()-2){
                marker.setReplacementText(String.valueOf(markerSetChars[i]));
                marker.setPrimaryMarker(true);
                continue;
            }
            marker.setReplacementText(String.valueOf(markerSetChars[markerSetChars.length-1]));
            marker.setPrimaryMarker(false);
        }
        return markers;
    }

    public static Optional<Marker> findMarkerByReplacementText(List<Marker> markers, String choice) {
        return markers.stream()
                .filter(marker -> marker.getReplacementText().toLowerCase().equals(choice))
                .findFirst();
    }
}
