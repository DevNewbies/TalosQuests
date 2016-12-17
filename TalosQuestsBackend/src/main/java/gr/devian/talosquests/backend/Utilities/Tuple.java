package gr.devian.talosquests.backend.Utilities;

import java.util.Objects;

/**
 * Created by Nikolas on 5/12/2016.
 */
public class Tuple<X, Y> extends Object{

    public final X left;
    public final Y right;

    public Tuple(X left, Y right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left + "," + right + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Tuple)) {
            return false;
        }
        Tuple<X, Y> other_ = (Tuple<X, Y>) other;
        return Objects.equals(other_.left, this.left) && Objects.equals(other_.right, this.right);
    }

    @Override
    public int hashCode() {
        final int prime = 13;
        int result = 1;
        result = prime * result + ((left == null) ? 0 : left.hashCode());
        result = prime * result + ((right == null) ? 0 : right.hashCode());
        return result;
    }

}