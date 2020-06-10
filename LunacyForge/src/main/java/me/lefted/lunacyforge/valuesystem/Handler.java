package me.lefted.lunacyforge.valuesystem;

public interface Handler<T> {

    /**
     * Maps a value to availabilities of children
     * 
     * @param newValue The latest value of the element
     * @return the availibilties of the childrenNames
     *         <p>
     *         Example:<br>
     *         Useful for sliders or comboboxes<br>
     *         private Handler<Integer> handler = new Handler<Integer>() {
     *         <p>
     * 
     *         <ul>
     *         public boolean[] handle(Integer newValue) {<br>
     *         <ul>
     *         if (newValue == 2) {<br>
     *         <ul>
     *         return new boolean[] { true, false, false };
     *         </ul>
     * 
     *         } else if (newValue == 1) {<br>
     *         <ul>
     *         return new boolean[] { false, true, false };
     *         </ul>
     *         } else {<br>
     *         <ul>
     *         return new boolean[] { false, false, true };
     *         </ul>
     *         }
     *         </ul>
     *         }<br>
     *         </ul>
     *         };
     *         <p>
     * 
     *         Example2:<br>
     *         If there were 5 children and all should be available if the value is true<br>
     *         private Handler<Boolean> handler2 = new Handler<Boolean>() {<br>
     *         <ul>
     *         public boolean[] handle(Boolean newValue) {<br>
     *         <ul>
     *         final boolean[] result = new boolean[5];<br>
     *         Arrays.fill(new boolean[5], newValue);<br>
     *         return result;<br>
     *         </ul>
     *         };
     *         </ul>
     *         };
     */
    boolean[] handle(T newValue);
}
