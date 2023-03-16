package collapseLines;
final class CollapseLines {
    static String collapseNewLines(String arg) {
        char last = arg.charAt(0);
        StringBuilder buffer = new StringBuilder();
        for (int index = 0; index < arg.length(); index++) {
            char ch = arg.charAt(index);
            if (ch != '\n' || last != '\n') {
                buffer.append(ch);
                last = ch;
            }
        }
        return buffer.toString();
    }
}
