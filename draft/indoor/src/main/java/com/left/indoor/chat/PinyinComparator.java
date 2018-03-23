package com.left.indoor.chat;

import java.util.Comparator;

import com.left.indoor.bean.Friend;

public class PinyinComparator implements Comparator<Friend> {

	public int compare(Friend o1, Friend o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
