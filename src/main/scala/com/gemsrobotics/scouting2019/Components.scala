package com.gemsrobotics.scouting2019

import scalafx.scene.control.TextField

object Components {
	val affirmativeGreen: String =
		"#37d964"

	def textField(field: String): TextField =
		new TextField {
			text = field
		}
}
