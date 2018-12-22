package symbols;
import java.util.*;
import lexer.*;
import inter.*;

public class Env {
	public final HashMap<Token, Id> table;
	protected Env prev;
	public final int envID;
	public Env(Env n) { table = new HashMap<>(); prev = n; envID = n == null ? 0 : (n.envID + 1); }

	public void put(Token w, Id i) { table.put(w, i); }

	public Id get(Token w) {
		for( Env e = this; e != null; e = e.prev ) {
			Id found = (Id)(e.table.get(w));
			if( found != null ) return found;
		}
		return null;
	}
	public void printHigherEnvIDs() {
		Env tmp = prev;
		while (tmp != null) {
			System.out.print("<" + tmp.envID + ">");
			tmp = tmp.prev;
		}
		System.out.println();
	}
}
