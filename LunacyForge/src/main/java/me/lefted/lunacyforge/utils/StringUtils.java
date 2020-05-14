package me.lefted.lunacyforge.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtils {

    public static int getNotNullDecimalCount(String input) {
	String part2 = input.split("\\.")[1];
	int len = part2.length();
	int j = 0;

	if (len > 1) {
	    char c = part2.charAt(len - 1 - j);

	    while (part2.charAt(len - 1 - j) == '0') {
		j++;
	    }
	}
	return len - j;
    }

    public static List<String> getParts(String string, int maxStringSize) {
	final List<String> results = new ArrayList<String>();

	final List<String> strings = Arrays.asList(string.split("\n"));
	for (String s : strings) {
	    results.addAll(cutNearEveryNth(s, maxStringSize));
	}
	return results;
    }

    public static List<String> cutNearEveryNth(String string, int size) {
	List<String> parts = new ArrayList<String>();
	int len = string.length();

	// passt es in eine Zeile
	if (len - 1 < size) {

	    // füge den ganzen string hinzu
	    parts.add(string);
	} else {

	    int startPos = 0;
	    int endPos = startPos + size;

	    boolean doing = true;
	    while (doing) {

		endPos = (Math.min(len, startPos + size));

		// wenn geschnitten werden muss
		if (endPos != len) {

		    // letzes zeichen bestimmen
		    char endChar = string.charAt(endPos);

		    // wenn es in ein wort schneiden würde
		    if (!(endChar == ' ' || endChar == '-')) {

			// ein vorheriges minus oder leerzeichen suchen
			int pos;
			for (pos = endPos - 1; string.charAt(pos) != ' ' && string.charAt(pos) != '-'; pos--) {
			}

			if (endChar == ' ') {
			    // bis vor das leerzeichen schneiden
			    endPos = pos - 1;
			    parts.add(string.substring(startPos, endPos));

			    // und es auch nicht in die nächte zeile machen
			    startPos = endPos + 2;
			} else { // wenn es ein minus ist
			    endPos = pos;
			    parts.add(string.substring(startPos, endPos + 1));
			    startPos = endPos + 1;
			}
		    } else {
			// wenn es ein leerzeichen ist
			if (endChar == ' ') {
			    parts.add(string.substring(startPos, endPos));

			    // das leerzeichen nicht in die nächte zeile machen
			    startPos = endPos + 1;
			} else { // wenn es ein minus ist
			    parts.add(string.substring(startPos, endPos));
			    startPos = endPos + 1;
			}
		    }
		} else { // wenn der rest rein passt
		    parts.add(string.substring(startPos, endPos));
		    doing = false;
		}
	    }
	}
	return parts;
    }
}
