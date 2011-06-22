import java.util.ArrayList;
import java.util.StringTokenizer;

public class CCOthers {
	public static String makekey(World world, int x, int y, int z) {
		return String.format("%s.%d.%d.%d",world.getType().name(),x,y,z);
	}

	public static String makekey(World world, Block b) {
		return makekey(world, b.getX(), b.getY(), b.getZ());
	}

	public static String implode(String[] ary, String delim) {
		String out = "";
		for (int i = 0; i < ary.length; i++) {
			if (i != 0)
				out = out + delim;
			out = out + ary[i];
		}
		return out;
	}

	public static String[] splitowners(String string) {
		String[] returnstring = string.split(",");
		return returnstring;
	}

	public static ArrayList<String> grablistof(String[] array, String returntype) {
		ArrayList<String> gList = new ArrayList<String>();
		ArrayList<String> oList = new ArrayList<String>();
		String[] arrayOfString = array;
		int j = array.length;
		for (int i = 0; i < j; i++) {
			String word = arrayOfString[i];
			if (word != null) {
				if (word.startsWith("g:"))
					gList.add(word.substring(2));
				else if (word.startsWith("o:"))
					oList.add(word.substring(2));
			}
		}
		if (returntype.equalsIgnoreCase("Groups")) {
			return gList;
		}
		return oList;
	}

	public static boolean inOwnersGroupConfig(ArrayList<String> alist,
			Player player) {
		if (alist.isEmpty()) {
			return false;
		}

		if (!player.hasNoGroups()) {
			String[] UserGroup = player.getGroups();
			if (UserGroup.length < 0) {
				return false;
			}

			for (String Groups : alist) {
				if (Groups.equalsIgnoreCase(UserGroup[0]))
					return true;
			}
		} else {
			String usergr = "default";

			for (String Groups : alist) {
				if (Groups.equalsIgnoreCase(usergr)) {
					return true;
				}

			}

		}

		return false;
	}

	public static boolean inOwnersOwnerConfig(ArrayList<String> alist,
			Player player) {
		if (alist.isEmpty()) {
			return false;
		}
		String PlayerName = player.getName();
		for (String Names : alist) {
			if (Names.equalsIgnoreCase(PlayerName)) {
				return true;
			}
		}

		return false;
	}

	public static String removeSpaces(String s) {
		StringTokenizer st = new StringTokenizer(s, " ", false);
		String t = "";
		while (st.hasMoreElements())
			t = t + st.nextElement();
		return t;
	}

	public static void shareerrordelete(Player player) {
		player.sendMessage("§c" + CCDataSource.ShareError);
		player.sendMessage("§c" + CCDataSource.CCCommand + " "
				+ CCDataSource.Delete + " o:Bob1234");
		player.sendMessage("§c" + CCDataSource.CCCommand + " "
				+ CCDataSource.Delete + " g:Mods");
	}

	public static void shareerroradd(Player player) {
		player.sendMessage("§c" + CCDataSource.ShareError);
		player.sendMessage("§c" + CCDataSource.CCCommand + " "
				+ CCDataSource.Add + " o:Bob1234");
		player.sendMessage("§c" + CCDataSource.CCCommand + " "
				+ CCDataSource.Add + " g:Mods");
	}
}