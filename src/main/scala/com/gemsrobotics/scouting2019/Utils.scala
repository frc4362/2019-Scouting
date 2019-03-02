package com.gemsrobotics.scouting2019

object Utils {
	implicit class bool2Int(bool: Boolean) {
		def toInt: Int =
			if (bool) {
				1
			} else {
				0
			}
	}
}
