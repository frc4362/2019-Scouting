package com.gemsrobotics.scouting2019

import scalafx.beans.property.IntegerProperty
import scalafx.geometry.Pos
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.text.{Font, FontWeight, TextAlignment}

protected class MyButton(text: String) extends Button(text) {
	prefWidth = 100
	prefHeight = 50
	font = Font("Consolas", FontWeight.Normal, 30)
}

class Incrementable(
     name: String,
     startingValue: Int = 0
) extends VBox {
	private val countLabel = new Label {
		text = startingValue.toString
		font = Font("Tahoma", FontWeight.Normal, 48)
		alignment = Pos.BaselineCenter
		textAlignment = TextAlignment.Center
		prefWidth = 215
	}

	private val nameLabel = new Label {
		text = name
		font = Font("Tahoma", FontWeight.Normal, 24)
		alignment = Pos.BaselineCenter
		textAlignment = TextAlignment.Center
	}

	val count = IntegerProperty(0)
	count.onChange { (_, _, newValue) =>
		countLabel.text = newValue.toString
	}

	val btns = Seq(
		new MyButton("-") {
			onMouseClicked = { _ =>
				if (count.value > 0) {
					count.value -= 1
				}
			}
		},
		new MyButton("+") {
			onMouseClicked = { _ =>
				count.value += 1
			}
		}
	)

	private val buttons = new HBox {
		spacing = 10
		children = btns
	}

	children = Seq(
		nameLabel,
		countLabel,
		buttons
	)
}
